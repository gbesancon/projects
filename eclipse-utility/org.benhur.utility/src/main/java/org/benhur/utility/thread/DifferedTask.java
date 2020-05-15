// Copyright (C) 2017 GBesancon

package org.benhur.utility.thread;

public class DifferedTask implements Runnable {
  protected final Runnable task;
  protected final long delay;

  public DifferedTask(final Runnable task, final long delay) {
    this.task = task;
    this.delay = delay;
  }

  public void run() {
    ThreadUtility.sleep(delay);
    task.run();
  }
}
