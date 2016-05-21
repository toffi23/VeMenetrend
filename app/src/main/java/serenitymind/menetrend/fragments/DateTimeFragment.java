package serenitymind.menetrend.fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import serenitymind.menetrend.R;
import serenitymind.menetrend.Schedule.Calendarium;

/**
 * Created by masko on 2016. 03. 21..
 */
public class DateTimeFragment extends Fragment
{
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.datetimefragment, container, false);

        setDefaultDateTime();
        initEvents();

        return view;
    }

    private void setDefaultDateTime()
    {
        TextView date = (TextView)view.findViewById(R.id.dateText);
        TextView time = (TextView)view.findViewById(R.id.timeText);

        try
        {
            Calendar defaultCal = Calendarium.getDefaultDateTime();
            int year = defaultCal.get(Calendar.YEAR);
            int month = defaultCal.get(Calendar.MONTH);
            int day = defaultCal.get(Calendar.DAY_OF_MONTH);
            int hour = defaultCal.get(Calendar.HOUR_OF_DAY);
            int minute = defaultCal.get(Calendar.MINUTE);

            String dateStr = Calendarium.formatDate(year, month, day);
            String timeStr = Calendarium.formatTime(hour,minute);

            Calendarium.calendar.set(year,month,day,hour,minute);
            date.setText(dateStr);
            time.setText(timeStr);
        }
        catch (NullPointerException ex)
        {
            Log.d("DATETIME","date or time caused nullreference");
        }
    }


    private void initEvents()
    {
        // Date
        final TextView date = (TextView)view.findViewById(R.id.dateText);
        try
        {
            date.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    DialogFragment dateDialog = new DatePickerFragment();
                    ((DatePickerFragment) dateDialog).setDateText(date);
                    dateDialog.show(getFragmentManager(),"dateDialog");
                }
            });


        }
        catch (NullPointerException ex)
        {
            Log.d("DATETIME", "TextView date is null.");
        }


        // Time
        final TextView time = (TextView)view.findViewById(R.id.timeText);
        try
        {
            time.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    DialogFragment timeDialog = new TimePickerFragment();
                    ((TimePickerFragment)timeDialog).setTimeText(time);
                    timeDialog.show(getFragmentManager(),"timeDialog");
                }
            });
        }
        catch (NullPointerException ex)
        {
            Log.d("DATETIME","TextView time is null.");
        }
    }

}