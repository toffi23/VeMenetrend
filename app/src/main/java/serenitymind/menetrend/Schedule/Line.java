package serenitymind.menetrend.Schedule;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Line implements Serializable
{
    private String name;
    private int lineNum;
    private HashMap<Integer,Track> tracks;
    private ArrayList<Start> starts;

    public Line(String pname)
    {
        name = pname;
        this.lineNum = this.createLineNumber();
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
        return String.format("%d",lineNum);
    }

    public int getLineNumber()
    {
        return lineNum;
    }

    public static class LineComparatorByName implements Comparator<Line>
    {
        @Override
        public int compare(Line lhs, Line rhs)
        {
            int result = lhs.getName().compareToIgnoreCase(rhs.getName());
            return result;
        }
    }

    public static class LineComparatorByNumber implements Comparator<Line>
    {
        @Override
        public int compare(Line lhs, Line rhs)
        {
            int result = Integer.compare(lhs.getLineNumber(),rhs.getLineNumber());
            return result;
        }
    }

    private int createLineNumber()
    {
        int lineNum;
        String tempName = name.substring(0,name.length()-1);

        Log.d("DBG-linenumber",name+" -> "+tempName);

        lineNum = Integer.parseInt(tempName,10);

        return lineNum;
    }

    public Station getFirstStation()
    {
        Track tmpTrack = null;
        for(Track t : tracks.values())
        {
            if(tmpTrack == null)
            {
                tmpTrack = t;
            }

            if(tmpTrack.getStops().size() < t.getStops().size())
            {
                tmpTrack = t;
            }
        }

        if(tmpTrack != null)
        {
            return tmpTrack.getStops().get(0).getStation();
        }

        return null;
    }

    public Station getLastStation()
    {
        Track tmpTrack = null;
        for(Track t : tracks.values())
        {
            if(tmpTrack == null)
            {
                tmpTrack = t;
            }

            if(tmpTrack.getStops().size() < t.getStops().size())
            {
                tmpTrack = t;
            }
        }

        if(tmpTrack != null)
        {
            return tmpTrack.getStops().get(tmpTrack.getStops().size()-1).getStation();
        }

        return null;
    }

}
