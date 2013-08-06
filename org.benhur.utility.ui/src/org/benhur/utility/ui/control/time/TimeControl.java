package org.benhur.utility.ui.control.time;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class TimeControl extends Composite
{
  public static final DecimalFormat INT_2_0_FMT = new DecimalFormat("00");
  public static final DecimalFormat INT_3_0_FMT = new DecimalFormat("000");
  protected static final String TIME_PATTERN = "hh:mm:ss.SSS";
  protected List<ITimeUpdatedListener> timeUpdatedListeners = new ArrayList<ITimeUpdatedListener>();
  protected Calendar currentCalendar = null;
  protected Calendar totalCalendar = null;
  protected Text field = null;

  public TimeControl(final Composite parent, int style)
  {
    super(parent, style);

    setLayout(new FillLayout());

    field = new Text(this, SWT.CENTER);
    field.setText(TIME_PATTERN);
    field.setTextLimit(TIME_PATTERN.length());
    field.setOrientation(SWT.LEFT_TO_RIGHT);
    field.setEnabled(true);
    field.setEditable(true);
    field.addKeyListener(new TimeKeyListener());

    currentCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
  }

  public void setCurrentTime(long time)
  {
    if (time == 0l || currentCalendar.getTimeInMillis() != time)
    {
      currentCalendar.setTimeInMillis(time);

      StringBuffer buffer = new StringBuffer();
      buffer.append(INT_2_0_FMT.format(currentCalendar.get(Calendar.HOUR_OF_DAY)));
      buffer.append(':');
      buffer.append(INT_2_0_FMT.format(currentCalendar.get(Calendar.MINUTE)));
      buffer.append(':');
      buffer.append(INT_2_0_FMT.format(currentCalendar.get(Calendar.SECOND)));
      buffer.append('.');
      buffer.append(INT_3_0_FMT.format(currentCalendar.get(Calendar.MILLISECOND)));
      field.setText(buffer.toString());
      field.setToolTipText(buffer.toString());
    }
  }

  public void notifyCr()
  {
    for (ITimeUpdatedListener timeUpdatedListener : timeUpdatedListeners)
    {
      timeUpdatedListener.timeChanged(getValueInMilliSeconds());
    }
  }

  protected long getValueInMilliSeconds()
  {
    String time = field.getText();

    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    calendar.setTimeInMillis(currentCalendar.getTimeInMillis());
    calendar.set(Calendar.AM_PM, Calendar.AM);

    String hh = time.substring(0, 2);
    if (hh.indexOf('h') == -1)
    {
      calendar.set(Calendar.HOUR, Integer.parseInt(hh));
    }
    String mm = time.substring(3, 5);
    if (mm.indexOf('m') == -1)
    {
      calendar.set(Calendar.MINUTE, Integer.parseInt(mm));
    }
    String ss = time.substring(6, 8);
    if (ss.indexOf('s') == -1)
    {
      calendar.set(Calendar.SECOND, Integer.parseInt(ss));
    }
    String SSS = time.substring(9, 12);
    if (SSS.indexOf('S') == -1)
    {
      calendar.set(Calendar.MILLISECOND, Integer.parseInt(SSS));
    }
    return calendar.getTimeInMillis();
  }

  public void addTimeUpdatedListener(ITimeUpdatedListener timeUpdatedListener)
  {
    timeUpdatedListeners.add(timeUpdatedListener);
  }

  public void removeTimeUpdatedListener(ITimeUpdatedListener timeUpdatedListener)
  {
    timeUpdatedListeners.remove(timeUpdatedListener);
  }

  protected class TimeKeyListener implements KeyListener
  {
    public TimeKeyListener()
    {}

    public void keyPressed(KeyEvent e)
    {}

    public void keyReleased(KeyEvent e)
    {
      if (e.character == SWT.CR)
      {
        notifyCr();
      }
    }
  }
}
