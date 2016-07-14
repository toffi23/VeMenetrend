package serenitymind.menetrend;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import serenitymind.menetrend.CustomInterfaces.ScheduleItemSelectedListener;
import serenitymind.menetrend.Schedule.Calendarium;
import serenitymind.menetrend.Schedule.DataBase;
import serenitymind.menetrend.Schedule.Line;
import serenitymind.menetrend.Schedule.Start;
import serenitymind.menetrend.Schedule.Station;
import serenitymind.menetrend.Schedule.Stop;
import serenitymind.menetrend.Schedule.XMLScheduleParser;
import serenitymind.menetrend.fragments.LineListFragment;
import serenitymind.menetrend.fragments.StationListFragment;


public class MainActivity extends FragmentActivity
                          implements ScheduleItemSelectedListener
{
    // Parser object
    private XMLScheduleParser parser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);


        if (findViewById(R.id.listFragmentContainer) != null) {
            if (savedInstanceState != null) {
                return;
            }

            // parse schedule xml file
            try {
                parser = new XMLScheduleParser("VeSchedule.xml", getBaseContext());
                parser.parse();
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }

            initList();

        }
    }

    private void initList() {
        LineListFragment lineListFragment = new LineListFragment();
        lineListFragment.setArguments(null); // with arguments null default list will be used

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.listFragmentContainer, lineListFragment)
                .commit();

    }

    public void onLinesButtonClick(View view) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Log.d("FRAGMENTMANAGER", "Count int linesbutton:" + getFragmentManager().getBackStackEntryCount());

        // create fragment and get context
        LineListFragment lineListFragment = new LineListFragment();
        lineListFragment.setArguments(getIntent().getExtras());

        // create transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.listFragmentContainer, lineListFragment);
        transaction.commit();

    }

    public void onStopsButtonClick(View view) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Log.d("FRAGMENTMANAGER", "Count int stopsbutton:" + getFragmentManager().getBackStackEntryCount());

        // create fragment and get context
        Bundle args = new Bundle();
        args.putInt(StationListFragment.MODESWITCH,StationListFragment.MODE_STATIONLIST_FULL);

        StationListFragment stationListFragment = new StationListFragment();
        stationListFragment.setArguments(args);

        // create transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.listFragmentContainer, stationListFragment);
        transaction.commit();


    }

    @Override
    public void onStationSelected(Station station) {
        Bundle args = new Bundle();
        args.putString(DataBase.STATIONKEY, station.getName());
        args.putString(LineListFragment.HEADERKEY, station.getName());

        Log.d("LINESFRAGMENT", "args / stationkey" + args.toString() + " / " + args.getString(DataBase.STATIONKEY));

        LineListFragment lineListFragment = new LineListFragment();
        lineListFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.listFragmentContainer, lineListFragment)
                .addToBackStack(null)
                .commit();


    }

    @Override
    public void onStopSelected(Stop stop) {
        Bundle args = new Bundle();
        args.putString(DataBase.STATIONKEY, stop.getStation().getName());
        args.putString(LineListFragment.HEADERKEY,stop.getStation().getName());

        LineListFragment lineListFragment = new LineListFragment();
        lineListFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.listFragmentContainer, lineListFragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onStartSelected(Start start)
    {
        Bundle args = new Bundle();
        args.putString(DataBase.LINEKEY,start.getParent().getName());
        args.putString(StationListFragment.HEADERKEY,start.getParent().getName()+" - "+start.getTimeStr());
        args.putInt(DataBase.STARTINDEX,start.getParent().getStarts().indexOf(start));
        args.putInt(StationListFragment.MODESWITCH,StationListFragment.MODE_STATIONLIST_STOPS);


        StationListFragment stationListFragment = new StationListFragment();
        stationListFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.listFragmentContainer,stationListFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onLineSelected(Line line, String header) {
        Bundle args = new Bundle();
        args.putString(DataBase.LINEKEY,line.getName());
        args.putString(StationListFragment.HEADERKEY,line.getName());
        args.putInt(StationListFragment.MODESWITCH,StationListFragment.MODE_STATIONLIST_STARTS);

        StationListFragment stationListFragment = new StationListFragment();
        stationListFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.listFragmentContainer,stationListFragment)
                .addToBackStack(null)
                .commit();



    }
}
