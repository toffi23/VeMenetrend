package serenitymind.menetrend.Schedule;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Masko on 2015.08.02..
 */
public class XMLScheduleParser
{
    private static final String Dsig = "PARSER";

    private XmlPullParser parser;
    private String fname;
    private Context con;

    public XMLScheduleParser(String filename,Context context) throws XmlPullParserException
    {
        parser = XmlPullParserFactory.newInstance().newPullParser();
        fname = filename;
        con = context;
    }

    public void parse() throws IOException, XmlPullParserException
    {
        if(parser != null)
        {
            parser.setInput(con.getAssets().open(fname),null);
        }

        if(parser == null)
        {
            //Log.d(Dsig, "Parser is null.");
            return;
        }

       // Log.d(Dsig,"Start parsing");

        int eventType = parser.getEventType();
        if(eventType == XmlPullParser.START_DOCUMENT)
        {
            eventType = parser.next();
            if(eventType == XmlPullParser.START_TAG)
            {
                String name = parser.getName();
                if(name.equalsIgnoreCase("Schedule"))
                {
                    //Log.d(Dsig,"<Schedule>");
                    parseSchedule(parser);
                }
            }
        }
    }

    private void parseSchedule(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        int eventType;
        String name;
        boolean parsing = true;

        while (parsing)
        {
            eventType = parser.next();
            name = parser.getName();

            switch (eventType)
            {
                case XmlPullParser.START_TAG:
                    if(name.equalsIgnoreCase("Stations"))
                    {
                        //Log.d(Dsig,"<Stations>");
                        parseStations(parser);
                    }else if(name.equalsIgnoreCase("Lines"))
                    {
                       // Log.d(Dsig,"<Lines>");
                        parseLines(parser);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if(name.equalsIgnoreCase("Schedule"))
                    {
                       //Log.d(Dsig,"</Schedule>");
                        parsing = false;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void parseStations(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        int eventType;
        String name;
        boolean parsing = true;

        Station station = null;
        int id = 0;

        while (parsing)
        {
            eventType = parser.next();
            name = parser.getName();

            switch (eventType)
            {
                case XmlPullParser.START_TAG:
                    if(name.equalsIgnoreCase("Station"))
                    {
                        String tmpid = parser.getAttributeValue(null, "id");
                        id = Integer.parseInt(tmpid);
                       // Log.d(Dsig, "<Station id=\"" + id + "\">");
                    }
                    break;
                case XmlPullParser.TEXT:
                    name = parser.getText();
                   // Log.d(Dsig,name);
                    station = new Station(id,name);
                    break;
                case XmlPullParser.END_TAG:
                    if(name.equalsIgnoreCase("Stations"))
                    {
                       // Log.d(Dsig,"</Stations>");
                        parsing = false;
                    }else if(name.equalsIgnoreCase("Station"))
                    {
                       // Log.d(Dsig,"</Station>");
                        if(station != null)
                        {
                            DataBase.addStation(station.getName(),station);
                            station = null;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void parseLines(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        int eventType;
        String name;
        boolean parsing = true;

        Line line = null;
        String id = null;

        while (parsing)
        {
            eventType = parser.next();
            name = parser.getName();

            switch (eventType)
            {
                case XmlPullParser.START_TAG:
                    if(name.equalsIgnoreCase("Line"))
                    {
                        id = parser.getAttributeValue(null, "name");
                        //Log.d(Dsig, "<Line name=\"" + id + "\">");
                        line = new Line(id);
                        //Log.d(Dsig, "<Tracks>");
                        HashMap<Integer, Track> tracks = parseTracks(parser,line);
                        line.setTracks(tracks);
                        ArrayList<Start> starts = parseStarts(parser,line);
                        line.setStarts(starts);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if(name.equalsIgnoreCase("Lines"))
                    {
                       // Log.d(Dsig,"</Lines>");
                        parsing = false;
                    }else if(name.equalsIgnoreCase("Line"))
                    {
                       // Log.d(Dsig,"</Line>");
                        if(line != null)
                        {
                            DataBase.addLine(id, line);
                            line = null;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private ArrayList<Start> parseStarts(XmlPullParser parser,Line parent) throws IOException, XmlPullParserException
    {
        int eventType;
        String name;
        boolean parsing = true;

        Start start = null;
        int trackid = 0;
        String active = null;
        ArrayList<Start> starts = new ArrayList<>();
        while (parsing)
        {
            eventType = parser.next();
            name = parser.getName();

            switch (eventType)
            {
                case XmlPullParser.START_TAG:
                    if (name.equalsIgnoreCase("Start"))
                    {
                        String tmpid = parser.getAttributeValue(null, "track");
                        trackid = Integer.parseInt(tmpid);
                        active = parser.getAttributeValue(null,"active");
                       // Log.d(Dsig, "<Start track=\"" + trackid + "\" active=\""+active+"\">");
                    }
                    break;
                case XmlPullParser.TEXT:
                    name = parser.getText();
                    if(name.matches("\\d\\d:\\d\\d"))
                    {
                      //  Log.d(Dsig, name);
                        start = new Start(trackid,active,name,parent);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (name.equalsIgnoreCase("Starts"))
                    {
                       // Log.d(Dsig, "</Starts>");
                        parsing = false;
                    } else if (name.equalsIgnoreCase("Start"))
                    {
                       // Log.d(Dsig, "</Start>");
                        if (start != null)
                        {
                            starts.add(start);
                            start = null;
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        return starts;
    }

    private HashMap<Integer, Track> parseTracks(XmlPullParser parser, Line line) throws IOException, XmlPullParserException
    {
        int eventType;
        String name;
        boolean parsing = true;

        Track track = null;
        HashMap<Integer,Track> tracks = new HashMap<>();
        int id = 0;

        while (parsing)
        {
            eventType = parser.next();
            name = parser.getName();

            switch (eventType)
            {
                case XmlPullParser.START_TAG:
                    if(name.equalsIgnoreCase("Track"))
                    {
                        String tmpid = parser.getAttributeValue(null, "id");
                        id = Integer.parseInt(tmpid);
                       // Log.d(Dsig, "<Track id=\"" + id + "\">");
                        track = new Track(id);
                        ArrayList<Stop> stops = parseStops(parser,line);
                        track.setStops(stops);
                        if(track != null)
                        {
                            tracks.put(id, track);
                            //track = null; why?? can't remember :/
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if(name.equalsIgnoreCase("Tracks"))
                    {
                       // Log.d(Dsig,"</Tracks>");
                        parsing = false;
                    }
                    break;
                default:
                    break;
            }
        }

        return tracks;
    }

    private ArrayList<Stop> parseStops(XmlPullParser parser, Line line) throws IOException, XmlPullParserException
    {
        int eventType;
        String name;
        boolean parsing = true;

        Stop stop = null;
        ArrayList<Stop> stops = new ArrayList<>();
        int id = -1;
        int delay = 0;

        while (parsing)
        {
            eventType = parser.next();
            name = parser.getName();

            switch (eventType)
            {
                case XmlPullParser.START_TAG:
                    if(name.equalsIgnoreCase("Stop"))
                    {
                        String tmpid = parser.getAttributeValue(null, "station");
                        id = Integer.parseInt(tmpid);
                       // Log.d(Dsig, "<Stop station=\"" + id + "\">");
                    }
                    break;
                case XmlPullParser.TEXT:
                    name = parser.getText();
                  //  Log.d(Dsig,name);
                    if(id != -1)
                    {
                        delay = Integer.parseInt(name);
                        Station station = DataBase.getStation(id);
                        stop = new Stop(station,delay);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if(name.equalsIgnoreCase("Track"))
                    {
                     //   Log.d(Dsig,"</Track>");
                        parsing = false;
                    }else if(name.equalsIgnoreCase("Stop"))
                    {
                      //  Log.d(Dsig,"</Stop>");
                        if(stop != null)
                        {
                            stops.add(stop);
                            stop.getStation().addLine(line);
                            stop = null;
                            id = -1;
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        return stops;
    }
}
