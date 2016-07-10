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

/**
 * Created by masko on 2016. 06. 06..
 */
public class LineListAdapter extends ArrayAdapter<Line>
{
    private boolean colorSwitch; // used for alternate between bg colors
    private boolean completeLineList;
    private ScheduleItemSelectedListener mCallback;

    private static class ViewHolder
    {
        public TextView lineNrView;
        public LinearLayout linearLayout;
        public TextView lineStartView;
        public TextView lineEndView;
        public LinearLayout linlayLines;
    }

    public LineListAdapter(Context context, ArrayList<Line> lineList,ScheduleItemSelectedListener _callee)
    {
        super(context,0,lineList);

        completeLineList = lineList.size() == DataBase.getLines().size() ? true : false;
        mCallback = _callee;
    }

    public View getView(int position, View convertView, ViewGroup parent)
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
            viewHolder.linlayLines = (LinearLayout)convertView.findViewById(R.id.linlay_lines);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
            viewHolder.linlayLines.removeAllViews();
        }

        final Line line = getItem(position);

        // TODO: 2016. 06. 06. Include start times for the list. Until then use the complete version
        if(completeLineList)
        {
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

            colorSwitch = false;
            createStartList(viewHolder.linlayLines,line);
        }

        return convertView;
    }

    private void createStartList(LinearLayout linlay, Line line)
    {
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

        for(int i = firstStart; i<line.getStarts().size(); i++)
        {
            s = line.getStarts().get(i);
            if(Calendarium.isStartActive(s))
            {
               linlay.addView(makeStartView(s));
            }
        }
    }

    private TextView makeStartView(final Start start)
    {
        TextView tv = new TextView(getContext());
        tv.setText(start.getTimeStr());
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
                Log.d("DBG-startlistadapter",start.toString());
                mCallback.onStartSelected(start);
            }
        });

        return tv;
    }

}
