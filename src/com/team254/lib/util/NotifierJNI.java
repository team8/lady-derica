package com.team254.lib.util;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class NotifierJNI
  extends JNIWrapper
{
  public NotifierJNI() {}
  
  public static native void initializeNotifierJVM(IntBuffer paramIntBuffer);
  
  public static native ByteBuffer initializeNotifier(ProcessQueue paramProcessQueue, IntBuffer paramIntBuffer);
  
  public static native void cleanNotifier(ByteBuffer paramByteBuffer, IntBuffer paramIntBuffer);
  
  public static native void updateNotifierAlarm(ByteBuffer paramByteBuffer, int paramInt, IntBuffer paramIntBuffer);
  
  public static abstract interface ProcessQueue
  {
    public abstract void apply(int paramInt, Object paramObject);
  }
}