package org.benhur.utility.thread;

public class DifferedTask implements Runnable
{
  protected final Runnable task;
  protected final long delay;

  public DifferedTask(final Runnable task, final long delay)
  {
    this.task = task;
    this.delay = delay;
  }

  @Override
  public void run()
  {
    try
    {
      Thread.sleep(delay);
      task.run();
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }
}
