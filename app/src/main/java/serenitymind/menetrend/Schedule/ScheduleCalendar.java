package serenitymind.menetrend.Schedule;

/**
 * Experiment for new handling of active days
 * Class will store 366 int as a code. Every bit of the code means an active.
 * For every day we set an integer with the bits 1 or 0 depends on which ACTIVE type is active
 * on that day
 */
public class ScheduleCalendar
{
    // Bit position in active code
    public static final int ACTIVE_WORK     = 0x1;
    public static final int ACTIVE_WEEKEND  = 0x2;
    public static final int ACTIVE_SCHOOL   = 0x4;
    public static final int ACTIVE_SATURDAY = 0x8;
    public static final int ACTIVE_SUNDAY   = 0x10;

    // Array to store active codes
    private int ActiveCodes[];

    public ScheduleCalendar()
    {
        ActiveCodes = new int[366];
        initActiveCodes();
    }

    /**
     * Function to set correct active codes for every day
     */
    private void initActiveCodes()
    {
        int code;

        // 01-01
        code = 0;
        code |= ACTIVE_SUNDAY;
        ActiveCodes[0] = code;

        // 01-02
        code = 0;
        code |= ACTIVE_SATURDAY;
        ActiveCodes[1] = code;

        // 01-03
        code = 0;
        code |= ACTIVE_SUNDAY;
        ActiveCodes[2] = code;

        // 01-04
        code = 0;
        code |= ACTIVE_WORK | ACTIVE_SCHOOL;
        ActiveCodes[3] = code;

        // 01-05
        code = 0;
        code |= ACTIVE_WORK | ACTIVE_SCHOOL;
        ActiveCodes[4] = code;

        // 01-06
        code = 0;
        code |= ACTIVE_WORK | ACTIVE_SCHOOL;
        ActiveCodes[5] = code;

        // 01-07
        code = 0;
        code |= ACTIVE_WORK | ACTIVE_SCHOOL;
        ActiveCodes[6] = code;

        // 01-08
        code = 0;
        code |= ACTIVE_WORK | ACTIVE_SCHOOL;
        ActiveCodes[7] = code;

        // 01-09
        code = 0;
        code |= ACTIVE_SATURDAY;
        ActiveCodes[8] = code;

        // 01-10
        code = 0;
        code |= ACTIVE_SUNDAY;
        ActiveCodes[9] = code;

        // 01-11
        code = 0;
        code |= ACTIVE_WORK | ACTIVE_SCHOOL;
        ActiveCodes[10] = code;
    }
}
