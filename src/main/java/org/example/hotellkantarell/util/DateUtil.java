package org.example.hotellkantarell.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static Date nDaysInFuture(Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 3);

        return calendar.getTime();
    }
}
