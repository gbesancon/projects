// Copyright (C) 2017 GBesancon

package org.benhur.utility.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool implements Executor {
  protected final ExecutorService mExecutor = Executors.newCachedThreadPool();

  public ThreadPool() {}

  public void execute(final Runnable task) {
    mExecutor.submit(task);
  }

  public void execute(final Runnable task, final long delay) {
    mExecutor.submit(new DifferedTask(task, delay));
  }

  public void execute(final Runnable task, final Callback callback) {
    CalledTask taskWithCallBack = new CalledTask(task, callback);
    mExecutor.execute(taskWithCallBack);
  }
}
