package serenitymind.menetrend.Schedule;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Line implements Serializable
{
    private String name;
    private HashMap<Integer,Track> tracks;
    private ArrayList<Start> starts;

    public Line(String pname)
    {
        name = pname;
    }

    public void setTracks(HashMap<Integer,Track> ptracks)
    {
        this.tracks = ptracks;
    }

    public String getName()
    {
        return name;
    }

    public void addTrack(Integer id,Track track)
    {
        tracks.put(id,track);
    }

    public Track getTrack(Integer id)
    {
        Track retTrack;

        if(id >= tracks.size())
        { // Mainly for test purpose
            retTrack = tracks.get(0);
            Log.d("PARAM_ERR", "Track index is too high!");
        }
        else
        {
            retTrack = tracks.get(id);
        }
        return retTrack;
    }

    public void setStarts(ArrayList<Start> starts)
    {
        this.starts = starts;
    }

    public ArrayList<Start> getStarts()
    {
        return starts;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public static class LineComparator implements Comparator<Line>
    {
        @Override
        public int compare(Line lhs, Line rhs)
        {
            int result = lhs.getName().compareToIgnoreCase(rhs.getName());
            return result;
        }
    }
}
