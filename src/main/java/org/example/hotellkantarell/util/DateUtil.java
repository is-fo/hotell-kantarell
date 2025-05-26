package org.example.hotellkantarell.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date setTime(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static boolean validDates(Date start, Date end) {

        Date today = Date.from(LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());

        boolean validDates = true;

        if (start != null && end != null) {
            if (start.after(end) || start.before(today) || end.equals(start)) {
                validDates = false;
            }
        }
        return validDates;
    }
}
