package org.benhur.utility.thread;

public class CalledTask implements Runnable
{
  protected final Runnable task;
  protected final ICallback callback;

  public CalledTask(Runnable task, ICallback callback)
  {
    this.task = task;
    this.callback = callback;
  }

  @Override
  public void run()
  {
    Exception catchedException = null;
    try
    {
      task.run();
    }
    catch (Exception e)
    {
      catchedException = e;
    }
    callback.taskPerformed(catchedException);
  }
}
