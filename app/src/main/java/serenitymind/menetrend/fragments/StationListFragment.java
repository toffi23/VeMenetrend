package serenitymind.menetrend.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import serenitymind.menetrend.CustomAdapters.StopListAdapter;
import serenitymind.menetrend.R;
import serenitymind.menetrend.Schedule.Calendarium;
import serenitymind.menetrend.Schedule.DataBase;
import serenitymind.menetrend.Schedule.Line;
import serenitymind.menetrend.Schedule.Start;
import serenitymind.menetrend.CustomAdapters.StartListAdapter;
import serenitymind.menetrend.Schedule.Station;
import serenitymind.menetrend.Schedule.Stop;
import serenitymind.menetrend.Schedule.Track;
import serenitymind.menetrend.CustomInterfaces.ScheduleItemSelectedListener;

/**
 * Created by masko on 2016. 03. 21..
 */
public class StationListFragment extends Fragment
{
    public static final String HEADERKEY = "HEADER";
    public static final String MODESWITCH = "MODESWITCH";
    public static final int MODE_STATIONLIST_FULL = 1;
    public static final int MODE_STATIONLIST_STOPS = 2;
    public static final int MODE_STATIONLIST_STARTS = 3;

    private ScheduleItemSelectedListener mCallbackStation;


    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        try
        {
            mCallbackStation = (ScheduleItemSelectedListener) activity;
        }
        catch (ClassCastException ex)
        {
            throw new ClassCastException(activity.toString() + " must implement onStationSelectedListener, onStopSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater,container,savedInstanceState);

        View view = inflater.inflate(R.layout.listfragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int modeSwitch = 0;

        if (getArguments() != null)
        {
            modeSwitch = getArguments().getInt(MODESWITCH);

        }

        switch(modeSwitch)
        {
            case MODE_STATIONLIST_FULL:
                stationList_Full();
                break;
            case MODE_STATIONLIST_STOPS:
                stationList_Stops();
                break;
            case MODE_STATIONLIST_STARTS:
                stationList_Starts();
                break;
            default:
                Log.d("DBG","Invalid mode switch in StationListFragment");
        }
    }

    /**
     * Indulási idők egy adott járathoz
     */
    private void stationList_Starts()
    {
        ListView listView = (ListView) getActivity().findViewById(R.id.fragmentList);

        String lineName = getArguments().getString(DataBase.LINEKEY);
        String headerText = getArguments().getString(HEADERKEY);
        ((TextView)getActivity().findViewById(R.id.headertext)).setText(headerText);

        ArrayList<ArrayList<Start>> startsList = new ArrayList<>();
        Line line = DataBase.getLine(lineName);

        if(line != null)
        {
            fillStartsList(startsList,line);
            StartListAdapter startListAdapter = new StartListAdapter(getActivity(),startsList,mCallbackStation);
            listView.setAdapter(startListAdapter);
        }
    }

    /**
     * Adott járathoz tartozó megállók
     */
    private void stationList_Stops()
    {
        ListView listView = (ListView) getActivity().findViewById(R.id.fragmentList);

        String lineName = getArguments().getString(DataBase.LINEKEY);
        int startIndex = getArguments().getInt(DataBase.STARTINDEX);
        String headerText = getArguments().getString(HEADERKEY);
        ((TextView)getActivity().findViewById(R.id.headertext)).setText(headerText);

        ArrayList<Stop> stoplist;
        Line line = DataBase.getLine(lineName);

        if(line != null)
        {
            Start start = line.getStarts().get(startIndex);
            stoplist = createStopList(start);
            StopListAdapter adapter = new StopListAdapter(getActivity(),stoplist,start);

            listView.setAdapter(adapter);
            setClickEventStopList(listView);
        }
    }

    /**
     * Összes megálló
     */
    private void stationList_Full()
    {
        ListView listView = (ListView) getActivity().findViewById(R.id.fragmentList);
        String headerText = "Full station list";

        ((TextView)getActivity().findViewById(R.id.headertext)).setText(headerText);

        ArrayList<Station> stations;
        ArrayAdapter<Station> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);

        stations = DataBase.getStations();
        Log.d("STATIONSFRAGMENT","Total stationlist");

        if(stations != null)
        {
            adapter.addAll(stations);
            adapter.sort(new Station.StationComparator());
            listView.setAdapter(adapter);
            setClickEventStationList(listView);
        }
    }


    private void fillStartsList(ArrayList<ArrayList<Start>> startsList, Line line)
    {
        ArrayList<Start> starts = new ArrayList<>();

        int hour = 0;
        int now = Calendarium.getHHMMinMins();
        int trackLength;
        int firstStart;
        Start s;
        for(firstStart = 0; firstStart < line.getStarts().size(); firstStart++)
        {
            s = line.getStarts().get(firstStart);
            trackLength = line.getTrack(s.getTrackIndex()).getLength();
            if(now <= s.getTimeInMins()+trackLength && Calendarium.isStartActive(s))
            {
                break;
            }
        }

        if(firstStart < line.getStarts().size()) hour = line.getStarts().get(firstStart).getTimeInMins()/60;
        for(int i = firstStart; i<line.getStarts().size(); i++)
        {

            s = line.getStarts().get(i);
            if(s.getTimeInMins()/60 != hour)
            {
                hour = s.getTimeInMins() / 60;
                if(starts.size()>0) startsList.add(starts);
                starts = new ArrayList<>();
            }
            if(Calendarium.isStartActive(s))
            {
                starts.add(s);

            }
        }
        if(starts.size()>0) startsList.add(starts);


    }

    private ArrayList<Stop> createStopList(Start start)
{
    ArrayList<Stop> stopList = new ArrayList<>();

    int currentTimeInMins = Calendarium.getHHMMinMins();
    int timeThreshold;

    Track track = start.getParent().getTrack(start.getTrackIndex());
    for(Stop stop : track.getStops())
    {
        timeThreshold = start.getTimeInMins() + stop.getDelay();

        if(currentTimeInMins <= timeThreshold)
        {
            stopList.add(stop);
        }
    }

    return stopList;
}

    private void setClickEventStopList(final ListView listView)
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Stop stop = (Stop) parent.getItemAtPosition(position);
                if(stop != null)
                {
                    mCallbackStation.onStopSelected(stop);
                }
            }
        });
    }

    private void setClickEventStationList(final ListView listView)
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Station station = (Station) parent.getItemAtPosition(position);
                if (station != null)
                {
                    mCallbackStation.onStationSelected(station);
                }
            }
        });
    }

}
