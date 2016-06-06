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
import serenitymind.menetrend.Schedule.Start;
import serenitymind.menetrend.fragments.StationListFragment;

/**
 * Created by masko on 2016. 05. 15..
 */
public class StartListAdapter extends ArrayAdapter<ArrayList<Start>>
{
    private boolean colorSwitch; // used for alternate between bg colors
    private StationListFragment.onStationSelectedListener mCallback;

    private static class ViewHolder
    {
        TextView hourView;
        LinearLayout linlayView;
    }

    public StartListAdapter(Context context, ArrayList<ArrayList<Start>> startsList, StationListFragment.onStationSelectedListener mCallback)
    {
        super(context,0,startsList);

        this.mCallback = mCallback;
        colorSwitch = false;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ArrayList<Start> starts = getItem(position);


        ViewHolder viewHolder;
        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.startlist_item,parent,false);
            viewHolder.hourView = (TextView)convertView.findViewById(R.id.hourText);
            viewHolder.linlayView = (LinearLayout)convertView.findViewById(R.id.linlay);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
            viewHolder.linlayView.removeAllViews();
        }
        colorSwitch = false;
        String hourStr;
        if(starts.size() > 0)
        {
            hourStr = String.format("%1$02d", starts.get(0).getTimeInMins() / 60);
            viewHolder.hourView.setText(hourStr);
            for (Start s : starts)
            {
                viewHolder.linlayView.addView(makeStartView(s));
            }
        }
        else
        {
            Log.d("DBG","starts is empty");
            hourStr = "-1:-1";
            viewHolder.hourView.setText(hourStr);
        }
        return convertView;
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
