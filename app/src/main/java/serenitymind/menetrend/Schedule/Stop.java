package serenitymind.menetrend.Schedule;

import java.util.Comparator;

/**
 * Created by Masko on 2015.08.03..
 */
public class Stop
{
    private Station station; // station id
    private int delay; // delay from start in mins

    public Stop(Station pstat, int pdelay)
    {
        station = pstat;
        delay = pdelay;
    }

    public int getDelay()
    {
        return delay;
    }

    @Override
    public String toString()
    {
        String tostr = station+"/"+delay;
        return tostr;
    }

    public Station getStation()
    {
        return this.station;
    }

    public static class StopComparator implements Comparator<Stop>
    {

        @Override
        public int compare(Stop lhs, Stop rhs)
        {
            int result = lhs.getStation().getName().compareToIgnoreCase(rhs.getStation().getName());

            return result;
        }
    }
}
