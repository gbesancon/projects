// Copyright (C) 2017 GBesancon

package org.benhur.utility.thread;

public class CalledTask implements Runnable {
  protected final Runnable task;
  protected final Callback callback;

  public CalledTask(final Runnable task, final Callback callback) {
    this.task = task;
    this.callback = callback;
  }

  public void run() {
    Exception catchedException = null;
    try {
      task.run();
    } catch (Exception e) {
      catchedException = e;
    }
    callback.taskPerformed(catchedException);
  }
}
