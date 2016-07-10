package serenitymind.menetrend.CustomAdapters;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import serenitymind.menetrend.R;
import serenitymind.menetrend.Schedule.Calendarium;
import serenitymind.menetrend.Schedule.DataBase;
import serenitymind.menetrend.Schedule.Line;
import serenitymind.menetrend.Schedule.Start;
import serenitymind.menetrend.CustomInterfaces.ScheduleItemSelectedListener;
import serenitymind.menetrend.Schedule.Station;
import serenitymind.menetrend.Schedule.Stop;
import serenitymind.menetrend.Schedule.Track;

/**
 * Created by masko on 2016. 06. 06..
 */
public class LineListAdapter extends ArrayAdapter<Line>
{
    private boolean colorSwitch; // used for alternate between bg colors
    private boolean completeLineList;
    private ScheduleItemSelectedListener mCallback;
    private Station station;

    private static class ViewHolder
    {
        public TextView lineNrView;
        public LinearLayout linearLayout;
        public TextView lineStartView;
        public TextView lineEndView;
    }

    private static class ViewHolderSpec
    {
        public TextView lineNrView;
        public LinearLayout linlayLines;
    }

    public LineListAdapter(Context context, ArrayList<Line> lineList, ScheduleItemSelectedListener _callee, Station _station)
    {
        super(context,0,lineList);

        completeLineList = _station == null?true:false;
        mCallback = _callee;
        station = _station;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(completeLineList)
        {
            ViewHolder viewHolder;
            if(convertView == null)
            {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.linelist_item,parent,false);
                viewHolder.lineNrView = (TextView)convertView.findViewById(R.id.lineNumberTextView);
                viewHolder.linearLayout = (LinearLayout)convertView.findViewById(R.id.lineListLinLay);
                viewHolder.lineStartView = (TextView)convertView.findViewById(R.id.lineStartTextView);
                viewHolder.lineEndView = (TextView)convertView.findViewById(R.id.lineEndTextView);

                convertView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder)convertView.getTag();
            }

            final Line line = getItem(position);

            viewHolder.lineNrView.setText(String.format("%d",line.getLineNumber()));
            viewHolder.lineStartView.setText(line.getFirstStation().getName());//.substring(0,3));
            viewHolder.lineEndView.setText(line.getLastStation().getName());//.substring(0,3));
            viewHolder.lineNrView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mCallback.onLineSelected(line,line.getName());
                }
            });
            viewHolder.linearLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mCallback.onLineSelected(line,line.getName());
                }
            });
        }
        else
        {
            ViewHolderSpec viewHolder;
            if(convertView == null)
            {
                viewHolder = new ViewHolderSpec();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.linelist_item_spec,parent,false);
                viewHolder.lineNrView = (TextView)convertView.findViewById(R.id.lineNumberTextView_spec);
                viewHolder.linlayLines = (LinearLayout)convertView.findViewById(R.id.linlay_lines_spec);

                convertView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolderSpec) convertView.getTag();
                viewHolder.linlayLines.removeAllViews();

            }

            final Line line = getItem(position);

            viewHolder.lineNrView.setText(String.format("%d",line.getLineNumber()));
            viewHolder.lineNrView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mCallback.onLineSelected(line,line.getName());
                }
            });

            colorSwitch = false;
            createStartList(viewHolder.linlayLines,line);
        }

        return convertView;
    }

    private void createStartList(LinearLayout linlay, Line line)
    {
        int currentTimeInMins = Calendarium.getHHMMinMins();
        int timeThreshold;

        int maxStopTime = 3;
        int stopTime = 0;

        for(Start start : line.getStarts())
        {
            Track track = start.getParent().getTrack(start.getTrackIndex());
            for (Stop stop : track.getStops())
            {
                if(stop.getStation() == station)
                {
                    timeThreshold = start.getTimeInMins() + stop.getDelay();

                    if (currentTimeInMins <= timeThreshold)
                    {
                        linlay.addView(makeStopView(start,stop));
                        stopTime++;
                        if(stopTime >= maxStopTime)return;
                    }
                    break;
                }
            }
        }
    }

    private TextView makeStopView(final Start start, final Stop stop)
    {
        TextView tv = new TextView(getContext());
        int stopTimeMin = start.getTimeInMins() + stop.getDelay();
        tv.setText(Calendarium.formatTime(stopTimeMin/60,stopTimeMin%60));
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tv.setPadding(20,0,20,0);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
        int color = colorSwitch?0xFFC2C2C2:0xFFA3A3A3;
        colorSwitch = colorSwitch?false:true;

        tv.setBackgroundColor(color);

        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCallback.onStartSelected(start);
            }
        });

        return tv;
    }

}
