package serenitymind.menetrend.Schedule;

import java.util.ArrayList;

/**
 * Created by Masko on 2015.08.03..
 */
public class Track
{
    private Integer id;
    private ArrayList<Stop> stops;

    public Track(Integer pid)
    {
        id = pid;
    }

    public Integer getId()
    {
        return id;
    }

    public void setStops(ArrayList<Stop> pstops)
    {
        stops = pstops;
    }

    public ArrayList<Stop> getStops()
    {
        return stops;
    }

    public void addStop(Stop stop)
    {
        stops.add(stop);
    }

    public void removeStop(Stop stop)
    {
        stops.remove(stop);
    }

    public int getLength()
    {
        return stops.get(stops.size()-1).getDelay();
    }
}
