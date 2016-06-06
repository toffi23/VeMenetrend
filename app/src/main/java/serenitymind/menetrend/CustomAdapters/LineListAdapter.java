package serenitymind.menetrend.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import serenitymind.menetrend.R;
import serenitymind.menetrend.Schedule.DataBase;
import serenitymind.menetrend.Schedule.Line;
import serenitymind.menetrend.fragments.LineListFragment;

/**
 * Created by masko on 2016. 06. 06..
 */
public class LineListAdapter extends ArrayAdapter<Line>
{
    private boolean completeLineList;
    private LineListFragment.onLineSelectedListener mCallback;

    private static class ViewHolder
    {
        public TextView lineNrView;
        public LinearLayout linearLayout;
        public TextView lineStartView;
        public TextView lineEndView;
    }

    public LineListAdapter(Context context, ArrayList<Line> lineList,LineListFragment.onLineSelectedListener _callee)
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

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        final Line line = getItem(position);

        // TODO: 2016. 06. 06. Include start times for the list. Until then use the complete version
        if(completeLineList || true)
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

        }

        return convertView;
    }

}
