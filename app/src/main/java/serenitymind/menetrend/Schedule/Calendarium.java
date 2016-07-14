package serenitymind.menetrend.Schedule;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by masko on 2016. 03. 21..
 */
public class Calendarium
{
    // static fields
    public static Calendar calendar;

    // static constants

    /**
     * _1 or _2 means specific handling for some lines where the same start has
     * different track in different part of the year
     */
    public static final String ACTIVE_WORK = "work";
    public static final String ACTIVE_WEEKEND = "weekend";
    public static final String ACTIVE_SCHOOL = "school";
    public static final String ACTIVE_SATURDAY = "sat";
    public static final String ACTIVE_SUNDAY = "sun";
    public static final String ACTIVE_WORK_1 = "work*1";
    public static final String ACTIVE_WEEKEND_1 = "weekend*1";
    public static final String ACTIVE_SCHOOL_1 = "school*1";
    public static final String ACTIVE_SATURDAY_1 = "sat*1";
    public static final String ACTIVE_SUNDAY_1 = "sun*1";
    public static final String ACTIVE_WORK_2 = "work*2";
    public static final String ACTIVE_WEEKEND_2 = "weekend*2";
    public static final String ACTIVE_SCHOOL_2 = "school*2";
    public static final String ACTIVE_SATURDAY_2 = "sat*2";
    public static final String ACTIVE_SUNDAY_2 = "sun*2";

    /* private internal members */
    // Beggining and end of summer pause
    private static final Calendar startSummer = Calendar.getInstance();
    private static final Calendar endSummer = Calendar.getInstance();
    // part1 of year (e.g. line 8)
    private static final Calendar part1start = Calendar.getInstance();
    private static final Calendar part1end = Calendar.getInstance();

    static
    {   /* Calendar month values are counted from 0 but days are from 1*/
        calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        startSummer.set(calendar.get((Calendar.YEAR)),5,15);
        endSummer.set(calendar.get(Calendar.YEAR),7,31);

        part1start.set(calendar.get(Calendar.YEAR),4,1);
        part1end.set(calendar.get(Calendar.YEAR),10,30);
    }

    public Calendarium()
    {
        /* constructor is empty because we use almost everything as static */
    }

    // static functions

    /**
     *
     * @param year
     * @param month - month is used as month+1 because Calendar class starts counting from 0 in month
     * @param day
     * @return
     */
    public static String formatDate(int year,int month, int day)
    {

        return String.format("%04d/%02d/%02d",year,month+1,day);
    }

    public static String formatTime(int hour, int  minute)
    {

        return String.format("%02d:%02d",hour,minute);
    }

    public static Calendar getDefaultDateTime()
    {
        return Calendar.getInstance();
    }

    /**
     * TODO: function is still preliminary. Shall be rework after final active handling is implemented.
     *
     * @param start
     * @return
     */
    public static boolean isStartActive(Start start)
    {
        String active = start.getActive();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        switch(active)
        {
            case ACTIVE_WORK:
                return
                (
                    dayOfWeek == Calendar.MONDAY ||
                    dayOfWeek == Calendar.TUESDAY ||
                    dayOfWeek == Calendar.WEDNESDAY ||
                    dayOfWeek == Calendar.THURSDAY ||
                    dayOfWeek == Calendar.FRIDAY
                );
            case ACTIVE_SCHOOL:
                return
                (
                    Calendarium.isSchoolTime() &&
                    (
                        dayOfWeek == Calendar.MONDAY ||
                        dayOfWeek == Calendar.TUESDAY ||
                        dayOfWeek == Calendar.WEDNESDAY ||
                        dayOfWeek == Calendar.THURSDAY ||
                        dayOfWeek == Calendar.FRIDAY
                    )
                );
            case ACTIVE_SATURDAY:
                return
                (
                    dayOfWeek == Calendar.SATURDAY
                );
            case ACTIVE_SUNDAY:
                return
                (
                    dayOfWeek == Calendar.SUNDAY
                );
            case ACTIVE_WEEKEND:
                return
                (
                    dayOfWeek == Calendar.SATURDAY ||
                    dayOfWeek == Calendar.SUNDAY
                );
            case ACTIVE_WORK_1:
                return
                (
                    dayOfWeek == Calendar.MONDAY ||
                    dayOfWeek == Calendar.TUESDAY ||
                    dayOfWeek == Calendar.WEDNESDAY ||
                    dayOfWeek == Calendar.THURSDAY ||
                    dayOfWeek == Calendar.FRIDAY
                ) && isPart1OfYear();
            case ACTIVE_SCHOOL_1:
                return
                (
                    dayOfWeek == Calendar.MONDAY ||
                    dayOfWeek == Calendar.TUESDAY ||
                    dayOfWeek == Calendar.WEDNESDAY ||
                    dayOfWeek == Calendar.THURSDAY ||
                    dayOfWeek == Calendar.FRIDAY
                ) && isSchoolTime() && isPart1OfYear();
            case ACTIVE_SATURDAY_1:
                return
                (
                    dayOfWeek == Calendar.SATURDAY
                ) && isPart1OfYear();
            case ACTIVE_SUNDAY_1:
                return
                (
                    dayOfWeek == Calendar.SUNDAY
                ) && isPart1OfYear();
            case ACTIVE_WEEKEND_1:
                return
                (
                    dayOfWeek == Calendar.SATURDAY ||
                    dayOfWeek == Calendar.SUNDAY
                ) && isPart1OfYear();
            case ACTIVE_WORK_2:
                return
                (
                    dayOfWeek == Calendar.MONDAY ||
                    dayOfWeek == Calendar.TUESDAY ||
                    dayOfWeek == Calendar.WEDNESDAY ||
                    dayOfWeek == Calendar.THURSDAY ||
                    dayOfWeek == Calendar.FRIDAY
                ) && isPart2OfYear();
            case ACTIVE_SCHOOL_2:
                return
                (
                    dayOfWeek == Calendar.MONDAY ||
                    dayOfWeek == Calendar.TUESDAY ||
                    dayOfWeek == Calendar.WEDNESDAY ||
                    dayOfWeek == Calendar.THURSDAY ||
                    dayOfWeek == Calendar.FRIDAY
                ) && isSchoolTime() && isPart2OfYear();
            case ACTIVE_SATURDAY_2:
                return
                (
                    dayOfWeek == Calendar.SATURDAY
                ) && isPart2OfYear();
            case ACTIVE_SUNDAY_2:
                return
                (
                    dayOfWeek == Calendar.SUNDAY
                ) && isPart2OfYear();
            case ACTIVE_WEEKEND_2:
                return
                (
                    dayOfWeek == Calendar.SATURDAY ||
                    dayOfWeek == Calendar.SUNDAY
                ) && isPart2OfYear();
            default:
                break;
        }

        return false;
    }

    /**
     *
     * @return true if currently there is school time (e.g. false during summer pause)
     */
    private static boolean isSchoolTime()
    {   // TODO: temporary solution until final date handling implementation
        startSummer.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
        endSummer.set(Calendar.YEAR,calendar.get(Calendar.YEAR));

        if((calendar.before(startSummer) || calendar.after(endSummer)))
        {
            return true;
        }

        return false;
    }

    /**
     *
     * @return true if it's first part of the year
     * and *1 exception is active
     */
    private static boolean isPart1OfYear()
    {
        /*4= IV.1-től IX. 30-ig a járatok a
        Csatárhegy úttól közlekednek
        a 2-es menetidőoszlop szerint*/

        part1start.set(Calendar.YEAR,calendar.YEAR);
        part1end.set(Calendar.YEAR,calendar.YEAR);

        if(calendar.before(part1start) || calendar.after(part1end))
        {
            return true;
        }

        return false;
    }

    /**
     *
     * @return true if it's first part of the year
     * and *2 exception is active
     */
    private static boolean isPart2OfYear()
    {
        /*4= IV.1-től IX. 30-ig a járatok a
        Csatárhegy úttól közlekednek
        a 2-es menetidőoszlop szerint*/

        part1start.set(Calendar.YEAR,calendar.YEAR);
        part1end.set(Calendar.YEAR,calendar.YEAR);

        if(calendar.before(part1start) || calendar.after(part1end))
        {
            return false;
        }

        return true;
    }

    public static int getHHMMinMins()
    {
        int minutes = 0;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int mins = calendar.get(Calendar.MINUTE);

        minutes += hour * 60 + mins;

        return minutes;
    }

}
