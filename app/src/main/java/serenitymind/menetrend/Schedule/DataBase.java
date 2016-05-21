package serenitymind.menetrend.Schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Masko on 2015.08.02..
 */
public class DataBase
{
    // static finals for EXTRA
    public static final String LINEKEY = "LINEKEY";
    public static final String STATIONKEY = "STATIONKEY";
    public static final String STARTINDEX = "STARTINDEX";

    // attributes
    private static HashMap<String,Station> stations;
    private static HashMap<String,Line> lines;

    static
    {
        stations = new HashMap<>();
        lines = new HashMap<>();
    }

    public static ArrayList<Station> getStations()
    {
        ArrayList<Station> retlist = new ArrayList<>(stations.values());
        return retlist;
    }

    public static void addStation(String id,Station pstation)
    {
        stations.put(id, pstation);
    }

    public static Station getStation(String id)
    {
        return stations.get(id);
    }

    public static ArrayList<Line> getLines()
    {
        ArrayList<Line> retlist = new ArrayList<>(lines.values());
        return retlist;
    }

    public static void addLine(String id, Line pline)
    {
        lines.put(id,pline);
    }

    public static Line getLine(String id)
    {
        return lines.get(id);
    }

    public static Station getStation(int id)
    {
        for(Station stat : getStations())
        {
            if(stat.getId() == id)return stat;
        }

        throw new NullPointerException("Station with id "+id+" is not found.");

    }
}
