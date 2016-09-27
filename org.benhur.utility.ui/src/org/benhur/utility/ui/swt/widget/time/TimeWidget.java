package org.benhur.utility.ui.swt.widget.time;

import java.text.DecimalFormat;
import java.util.Calendar;

import org.benhur.utility.date.DateUtility;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class TimeWidget extends Composite
{
  public static final DecimalFormat INT_2_0_FMT = new DecimalFormat("00"); //$NON-NLS-1$
  public static final DecimalFormat INT_3_0_FMT = new DecimalFormat("000"); //$NON-NLS-1$
  public final static int SECOND_IN_MILLISECONDS = 1000;
  public final static int MINUTE_IN_MILLISECONDS = 60 * SECOND_IN_MILLISECONDS;
  public final static int HOUR_IN_MILLISECONDS = 60 * MINUTE_IN_MILLISECONDS;
  protected ITimeChangeListener timeChangeListener = null;
  protected Calendar currentCalendar = null;
  protected Calendar totalCalendar = null;
  protected Text field = null;
  protected static final String PATTERN = "hh:mm:ss.SSS";

  public TimeWidget(final Composite parent, int style)
  {
    super(parent, style);

    setLayout(new FillLayout());

    field = new Text(this, SWT.CENTER);
    field.setText(PATTERN);
    field.setTextLimit(PATTERN.length());
    field.setOrientation(SWT.LEFT_TO_RIGHT);
    field.setEnabled(true);
    field.setEditable(true);
    field.addListener(SWT.Verify, new TimeVerifyListener());
    field.addKeyListener(new TimeKeyListener());

    currentCalendar = DateUtility.getGMTCalendar();
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
    if (timeChangeListener != null)
    {
      timeChangeListener.timeChanged(getValueInMilliSeconds());
    }
  }

  protected long getValueInMilliSeconds()
  {
    String time = field.getText();

    Calendar calendar = DateUtility.getGMTCalendar();
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

  public void setTimeChangeListener(ITimeChangeListener timeChangeListener)
  {
    this.timeChangeListener = timeChangeListener;
  }

  protected class TimeVerifyListener implements Listener
  {
    protected boolean ignore = false;

    public TimeVerifyListener()
    {}

    @Override
    public void handleEvent(Event event)
    {
      if (!ignore)
      {
        event.doit = false;

        StringBuffer buffer = new StringBuffer(event.text);
        char[] chars = new char[buffer.length()];

        buffer.getChars(0, chars.length, chars, 0);

        if (event.character == SWT.BS || event.character == SWT.DEL)
        {
          for (int i = event.start; i < event.end; i++)
          {
            if (i < PATTERN.length())
            {
              buffer.append(PATTERN.charAt(i));
            }
          }

          field.setSelection(event.start, event.start + buffer.length());
          ignore = true;

          field.insert(buffer.toString());
          ignore = false;

          if (event.character == SWT.BS)
          {
            field.setSelection(event.start, event.start);
          }
          else if (event.character == SWT.DEL)
          {
            field.setSelection(event.start + 1, event.start + 1);
          }
        }
        else
        {
          int start = event.start;
          if (start <= PATTERN.length())
          {
            int index = 0;
            for (int i = 0; i < chars.length; i++)
            {
              if (start + index == 2 || start + index == 5)
              {
                if (chars[i] == ':')
                {
                  index++;
                  continue;
                }
                buffer.insert(index++, ':');
              }
              else if (start + index == 8)
              {
                if (chars[i] == '.')
                {
                  index++;
                  continue;
                }
                buffer.insert(index++, '.');
              }
              if (chars[i] < '0' || chars[i] > '9')
                return;
              // if (start + index == 0 && chars[i] > '2')
              // return; /* [h]h */
              if (start + index == 3 && chars[i] > '5')
                return; /* [m]m */
              if (start + index == 5 && chars[i] > '5')
                return; /* [s]s */
              // if (false)
              // return; /* [S]SS */
              index++;
            }

            String newText = buffer.toString();
            int length = newText.length();
            StringBuffer date = new StringBuffer(field.getText());
            date.replace(event.start, event.start + length, newText);
            String hh = date.substring(0, 2);
            if (hh.indexOf('h') == -1)
            {
              int hour = Integer.parseInt(hh);
              // int maxHour = mCalendar.getActualMaximum(Calendar.HOUR_OF_DAY);
              // if (hour < 0 || hour > maxHour)
              // return;
              currentCalendar.set(Calendar.HOUR_OF_DAY, hour);
            }
            String mm = date.substring(3, 5);
            if (mm.indexOf('m') == -1)
            {
              int minute = Integer.parseInt(mm);
              int maxMinute = currentCalendar.getActualMaximum(Calendar.MINUTE);
              if (minute < 0 || minute > maxMinute)
                return;
              currentCalendar.set(Calendar.MINUTE, minute);
            }
            String ss = date.substring(6, 8);
            if (ss.indexOf('s') == -1)
            {
              int second = Integer.parseInt(ss);
              int maxSecond = currentCalendar.getActualMaximum(Calendar.SECOND);
              if (second < 0 || second > maxSecond)
                return;
              currentCalendar.set(Calendar.SECOND, second);
            }
            String SSS = date.substring(9, 12);
            if (SSS.indexOf('S') == -1)
            {
              int millisecond = Integer.parseInt(SSS);
              int maxMillisecond = currentCalendar.getActualMaximum(Calendar.MILLISECOND);
              if (millisecond < 0 || millisecond > maxMillisecond)
                return;
              currentCalendar.set(Calendar.MILLISECOND, millisecond);
            }
            field.setSelection(event.start, event.start + length);
            ignore = true;
            field.insert(newText);
            ignore = false;
          }
        }
      }
    }
  }

  protected class TimeKeyListener implements KeyListener
  {
    public TimeKeyListener()
    {}

    @Override
    public void keyPressed(KeyEvent e)
    {}

    @Override
    public void keyReleased(KeyEvent e)
    {
      if (e.character == SWT.CR)
      {
        notifyCr();
      }
    }
  }
}
