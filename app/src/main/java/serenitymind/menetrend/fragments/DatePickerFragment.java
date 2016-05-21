package serenitymind.menetrend.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import serenitymind.menetrend.Schedule.Calendarium;

/**
 * Created by masko on 2016. 03. 21..
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    Calendar c;
    TextView dateText = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day)
    {
        Calendarium.calendar.set(year,month,day);

        String date = Calendarium.formatDate(year,month,day);
        if(dateText != null)dateText.setText(date);

    }

    public void setDateText(TextView pdateText)
    {
        this.dateText = pdateText;
    }



}
