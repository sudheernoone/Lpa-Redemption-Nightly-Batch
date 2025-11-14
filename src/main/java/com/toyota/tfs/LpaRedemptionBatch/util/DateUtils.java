package com.toyota.tfs.LpaRedemptionBatch.util;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;
@Component
public class DateUtils {
    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public static Date removeDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE,-days);
        return cal.getTime();
    }
    
    public String getDateTime() {
    	return LocalDateTime.now().toString();
    }
}
