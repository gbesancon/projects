package org.benhur.utility.date;

import java.util.Calendar;
import java.util.TimeZone;

public class DateUtility
{
  public static final TimeZone TIMEZONE_GMT = TimeZone.getTimeZone("GMT");

  public static final long LOCAL_TIMEZONE_GMT_OFFSET = TimeZone.getDefault().getRawOffset()
      + TimeZone.getDefault().getDSTSavings();

  private DateUtility()
  {}

  public static Calendar getGMTCalendar()
  {
    return Calendar.getInstance(TIMEZONE_GMT);
  }
}
