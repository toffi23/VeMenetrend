package serenitymind.menetrend.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import serenitymind.menetrend.CustomAdapters.LineListAdapter;
import serenitymind.menetrend.R;
import serenitymind.menetrend.Schedule.DataBase;
import serenitymind.menetrend.Schedule.Line;
import serenitymind.menetrend.Schedule.Station;
import serenitymind.menetrend.CustomInterfaces.ScheduleItemSelectedListener;

/**
 * Created by masko on 2016. 03. 21..
 */
public class LineListFragment extends Fragment
{
    public static final String HEADERKEY = "HEADERKEY";

    ScheduleItemSelectedListener mCallback;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mCallback = (ScheduleItemSelectedListener)activity;
        }
        catch (ClassCastException ex)
        {
            throw new ClassCastException(ex.toString()+" must implement onLineSelectedListener");
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
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        LineListAdapter adapter;
        ListView listView = (ListView) getActivity().findViewById(R.id.fragmentList);

        ArrayList<Line> lineList = null;
        String stationName = null;
        String headerText;

        if(getArguments() != null)
        {
            stationName = getArguments().getString(DataBase.STATIONKEY);
            headerText = getArguments().getString(HEADERKEY);
        }
        else
        {
            headerText = "Full line list";
        }

        if(headerText != null)
        {
            ((TextView)getActivity().findViewById(R.id.headertext)).setText(headerText);
        }

        if(stationName == null)
        {
            lineList = DataBase.getLines();
        }
        else
        {
            Station station = DataBase.getStation(stationName);
            if(station != null)
            {
                lineList = station.getLines();
            }
        }

        if (lineList != null) {
            adapter = new LineListAdapter(getActivity(),lineList,mCallback);
            adapter.sort(new Line.LineComparatorByNumber());
            listView.setAdapter(adapter);
        } else Log.d("LINESFRAGMENT", "lineList is null");

    }

    private void setClickEventLineList(final ListView listView)
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Line line = (Line) parent.getItemAtPosition(position);
                if (line != null)
                {
                    mCallback.onLineSelected(line, line.getName());
                }
            }
        });
    }

}
