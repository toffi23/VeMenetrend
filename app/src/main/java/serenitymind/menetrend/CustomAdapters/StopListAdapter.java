package serenitymind.menetrend.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import serenitymind.menetrend.R;
import serenitymind.menetrend.Schedule.Start;
import serenitymind.menetrend.Schedule.Stop;
import serenitymind.menetrend.fragments.StationListFragment;

/**
 * Created by masko on 2016. 05. 16..
 */
public class StopListAdapter extends ArrayAdapter<Stop>
{
    private boolean colorSwitch; // used for alternate between bg colors
    private Start start; // start is needed to determine time for stops

    private static class ViewHolder
    {
        TextView stationNameView;
        TextView arriveTimeView;
    }

    public StopListAdapter(Context context, ArrayList<Stop> stopList, Start pstart)
    {
        super(context,0,stopList);

        colorSwitch = false;
        start = pstart;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
       Stop stop = getItem(position);

        // FIXME: 2016. 06. 06. Restore viewholder pattern and solve coloring issue
        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.stoplist_item,parent,false);
        }

        viewHolder.stationNameView = (TextView)convertView.findViewById(R.id.stationNameTV);
        viewHolder.arriveTimeView = (TextView)convertView.findViewById(R.id.arriveTimeTV);

        if(stop != null)
        {
            String sname = stop.getStation().getName();
            int time = start.getTimeInMins() + stop.getDelay();
            String strTime = String .format("%02d:%02d",time/60,time%60);

            viewHolder.stationNameView.setText(sname);
            viewHolder.arriveTimeView.setText(strTime);

            int color = colorSwitch?0xFFC2C2C2:0xFFA3A3A3;
            colorSwitch = colorSwitch?false:true;
            convertView.setBackgroundColor(color);
        }

        return convertView;
    }
}
