package org.whirlplatform.server.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtils {


    /*Преобразует 18-числовую дату LDAP в миллисекунды, с которыми можно работать в Java: new Date(milliseconds)*/
    public static long javaLdap18dTimeToMilliseconds(long nanoseconds) {
        long mills = (nanoseconds / 10000000);
        long unix = (((1970 - 1601) * 365) - 3 + Math.round((1970 - 1601) / 4)) * 86400L;
        long timeStamp = mills - unix;
        Date date = new Date(timeStamp * 1000L); // *1000 is to convert seconds to milliseconds
        return date.getTime();
    }

    /*Преобразует миллисекунды в 18-числовое представление даты LDAP */
    public static long javaMillisecondsToLdap18dTime(long timeInMillis) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTimeZone(TimeZone.getTimeZone("UTC"));
        c.set(1601, 0, 1);
        long t2 = c.getTimeInMillis();
        long ldap18dTime = Math.abs(timeInMillis - t2) * 10000;
        return ldap18dTime;
    }
    /*Эти две процедуры (javaLdap18dTimeToMilliseconds и javaMillisecondsToLdap18dTime)
     * выполняют обратимое преобразование. С точностью не более чем 1000 наносекунд */

}
