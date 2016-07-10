package serenitymind.menetrend.CustomInterfaces;

import serenitymind.menetrend.Schedule.Line;
import serenitymind.menetrend.Schedule.Start;
import serenitymind.menetrend.Schedule.Station;
import serenitymind.menetrend.Schedule.Stop;

/**
 * Created by masko on 2016. 07. 10..
 */
public interface ScheduleItemSelectedListener
{
    void onLineSelected(Line line, String header);
    void onStationSelected(Station station);
    void onStopSelected(Stop stop);
    void onStartSelected(Start start);
}
