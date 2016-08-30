package com.team8.lib.util;

import edu.wpi.first.wpilibj.Timer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.Semaphore;

import com.team8.lib.util.NotifierJNI.ProcessQueue;

/**
 * Class to utilize timer interrupts from the FPGA to schedule one time or periodic
 * tasks. The standard JRE that runs on the roboRIO is not very good at obeying
 * timing (nor is it designed to), utilizing the timer in the FPGA to run timed loops
 * may be a better choice.
 */
public class Notifier {
	/**
	* This is done to store the JVM variable in the NotifierJNI
	* This is done because the HAL must have access to the JVM variable
	* in order to attach the newly spawned thread when an interrupt is fired.
	*/
	static {
		try{
			ByteBuffer status = ByteBuffer.allocateDirect(4);
			status.order(ByteOrder.LITTLE_ENDIAN);
			NotifierJNI.initializeNotifierJVM(status.asIntBuffer());
			HALUtil.checkStatus(status.asIntBuffer());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	* Anonymous implementation of a ProcessQueue. The apply() method
	* is used as a callback from the HAL for timer interrupts.
	*/
	static ProcessQueue s_notifier_process_queue = new ProcessQueue() {
		@Override
		public void apply(int mask, Object param) {
			Notifier.processQueue(mask, param);
		}
	};

	static private Notifier s_timer_queue_head;
	static private int s_ref_count = 0;
	static private Object s_queue_semaphore = new Object();

	protected static ByteBuffer s_notifier = null;
	public double m_expiration_time = 0;

	protected Object m_param;
	protected TimerEventHandler m_handler;
	public double m_period = 0;
	public boolean m_periodic = false;
	public boolean m_queued = false;
	public Notifier m_next_event;
	public Semaphore m_handler_semaphore = new Semaphore(1);

	/**
	* Notifier constructor. Construct a Notifier given a handler and param to pass.
	*
	* The encoder will start counting immediately.
	*
	* @param handler
	*            The TimerEventHandler that should be called when this notifier expires.
	* @param param
	*            An object which will be passed to the handler when it is called
	*/
	public Notifier(TimerEventHandler handler, Object param) {
		m_handler = handler;
		m_param = param;
		synchronized(s_queue_semaphore) {
		  if (s_ref_count == 0) {
			  try{
				  ByteBuffer status = ByteBuffer.allocateDirect(4);
				  status.order(ByteOrder.LITTLE_ENDIAN);
				  s_notifier = NotifierJNI.initializeNotifier(s_notifier_process_queue, status.asIntBuffer());
				  HALUtil.checkStatus(status.asIntBuffer());
			  } catch(Exception e) {
				  e.printStackTrace();
			  }
		  }
		  s_ref_count++;
		}
	}

	/**
	* Update the alarm hardware to reflect the current first element in the queue.
	* Compute the time the next alarm should occur based on the current time and the
	* period for the first element in the timer queue.
	* WARNING: this method does not do synchronization! It must be called from somewhere
	* that is taking care of synchronizing access to the queue.
	*/
	public static void updateAlarm() {
		if (s_timer_queue_head != null)
		{
			try{
				ByteBuffer status = ByteBuffer.allocateDirect(4);
				status.order(ByteOrder.LITTLE_ENDIAN);
				NotifierJNI.updateNotifierAlarm(s_notifier, (int)(s_timer_queue_head.m_expiration_time * 1e6), status.asIntBuffer());
				HALUtil.checkStatus(status.asIntBuffer());
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void insertInQueue(boolean reschedule) {
		if (reschedule) {
			m_expiration_time += m_period;
		} else {
			m_expiration_time = Timer.getFPGATimestamp() + m_period;
		}
		if (s_timer_queue_head == null || s_timer_queue_head.m_expiration_time >= this.m_expiration_time)
		{
			// the queue is empty or greater than the new entry
			// the new entry becomes the first element
			this.m_next_event = s_timer_queue_head;
			s_timer_queue_head = this;
			if (!reschedule) {
				// since the first element changed, update alarm, unless we already plan to
				updateAlarm();
			}
		} else {
			Notifier last = s_timer_queue_head;
			Notifier cur = s_timer_queue_head.m_next_event;
			boolean looking = true;
			while (looking) {
				if (cur == null || cur.m_expiration_time > this.m_expiration_time) {
					last.m_next_event = this;
					this.m_next_event = cur;
					looking = false;
				}
			}
		}
		m_queued = true;
	}

	public void deleteFromQueue() {
		if (m_queued)
		{
			m_queued = false;
			if (s_timer_queue_head == null) {
				return;
			}
			if (s_timer_queue_head == this) {
				// remove the first item in the list - update the alarm
				s_timer_queue_head = this.m_next_event;
				updateAlarm();
			}
			else {
				for (Notifier n = s_timer_queue_head; n != null; n = n.m_next_event) {
					if (n.m_next_event == this) {
						// this element is the next element from *n from the queue
						n.m_next_event = this.m_next_event; // point around this one
					}
				}
			}
		}
	}

	public void startSingle(double delay) {
		synchronized(s_queue_semaphore) {
			m_periodic = false;
			m_period = delay;
			deleteFromQueue();
			insertInQueue(false);
		}
	}

	public void startPeriodic(double period) {
		synchronized(s_queue_semaphore) {
			m_periodic = true;
			m_period = period;
			deleteFromQueue();
			insertInQueue(false);
		}
	}
  
	public void stop() {
		synchronized(s_queue_semaphore) {
			deleteFromQueue();
		}
		try {
			m_handler_semaphore.acquire();
			m_handler_semaphore.release();
		} catch (InterruptedException e) {
		}
	}

	static public void processQueue(int mask, Object param) {
		Notifier current;
		// keep processing past events until no more
		while (true)
		{
			synchronized(s_queue_semaphore) {
				double current_time = Timer.getFPGATimestamp();
				current = s_timer_queue_head;
				if (current == null || current.m_expiration_time > current_time) {
					break; // no more timer events to process
				}
				// need to process this entry
				s_timer_queue_head = current.m_next_event;
				if (current.m_periodic) {
					// if periodic, requeue the event
					// compute when to put into queue
					current.insertInQueue(true);
				}
				else {
					// not periodic; removed from queue
					current.m_queued = false;
				}
				// Take handler semaphore while holding queue semaphore to make sure
				//  the handler will execute to completion in case we are being deleted.
				try {
					current.m_handler_semaphore.acquire();
				} catch (InterruptedException e) {
				}
			}
			current.m_handler.update(current.m_param); // call the event handler
			current.m_handler_semaphore.release();
		}
		// reschedule the first item in the queue
		synchronized(s_queue_semaphore) {
			updateAlarm();
		}
	}
}