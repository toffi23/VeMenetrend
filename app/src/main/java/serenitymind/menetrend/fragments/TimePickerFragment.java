package serenitymind.menetrend.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import serenitymind.menetrend.Schedule.Calendarium;

/**
 * Created by masko on 2016. 03. 21..
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{
    Calendar c;
    TextView timeText = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        return new TimePickerDialog(getActivity(),this,hour,minute,true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        Calendarium.calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        Calendarium.calendar.set(Calendar.MINUTE, minute);

        String time = Calendarium.formatTime(hourOfDay,minute);
        if(timeText != null)timeText.setText(time);

        Log.d("HHMM",String.valueOf(Calendarium.getHHMMinMins()));
    }

    public void setTimeText(TextView ptimeText)
    {
        this.timeText = ptimeText;
    }

}
