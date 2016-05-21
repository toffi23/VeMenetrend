package serenitymind.menetrend.Schedule;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by Masko on 2015.08.09..
 */
public class Start
{
    private Integer track;
    private String active;
    private Line parent;
    private int time; // start time in minutes

    private final static SimpleDateFormat format = new SimpleDateFormat("hh:mm");

    public Start(Integer ptrack, String pactive, String ptime, Line _parent)
    {
        track = ptrack;
        active = pactive;
        parent = _parent;

        try
        {
            String[] splittime =  ptime.split(":");
            int hh = Integer.parseInt(splittime[0]);
            int mm = Integer.parseInt(splittime[1]);

            time = hh * 60 + mm;
        }catch (Exception e)
        {
            Log.d("TIMEPARSE","Unable to parse time.");
            e.printStackTrace();
        }
    }


    public Line getParent()
    {
        return parent;
    }

    public Integer getTrackIndex()
    {
        return track;
    }

    public String getActive()
    {
        return active;
    }

    public int getTimeInMins()
    {
        return time;
    }

    public String getTimeStr()
    {
        int hours = time / 60;
        int mins = time % 60;
        String strtime = String.format("%02d:%02d",hours,mins);

        return strtime;
    }

    @Override
    public String toString()
    {
        return track.toString() + "/" + active + "/" + getTimeStr();
    }
}
