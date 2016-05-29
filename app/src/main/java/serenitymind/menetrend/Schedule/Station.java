package serenitymind.menetrend.Schedule;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by Masko on 2015.08.02..
 */
public class Station
{
    private Integer id;
    private String name;
    private ArrayList<Line> lines;

    public Station(int pid, String pname)
    {
        lines = new ArrayList<>();
        this.id = pid;
        this.name = pname;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public Integer getId()
    {
        return id;
    }

    public void addLine(Line line)
    {
        if(lines.contains(line) == false)
        {
            lines.add(line);
        }
    }

    public ArrayList<Line> getLines()
    {
        return lines;
    }

    public static class StationComparator implements Comparator<Station>
    {

        @Override
        public int compare(Station lhs, Station rhs)
        {
            Collator collator = Collator.getInstance(Locale.getDefault());
            collator.setStrength(Collator.SECONDARY);
            int result = collator.compare(lhs.getName(),rhs.getName());

            return result;
        }
    }
}
