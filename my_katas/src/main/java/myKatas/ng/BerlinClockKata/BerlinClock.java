package myKatas.ng.BerlinClockKata;

/**
 * Created by mba on 24/04/2016.
 */
public class BerlinClock {

    /**
     *
     * Exercise :
     *
     * Create a representation of the Berlin Clock for a given time (hh::mm:ss).

     The Berlin Uhr (Clock) is a rather strange way to show the time.
     On the top of the clock there is a yellow lamp that blinks on/off every two seconds.
     The time is calculated by adding rectangular lamps.

     The top two rows of lamps are red. These indicate the hours of a day. In the top row there are 4 red lamps.
     Every lamp represents 5 hours. In the lower row of red lamps every lamp represents 1 hour.
     So if two lamps of the first row and three of the second row are switched on that indicates 5+5+3=13h or 1 pm.

     The two rows of lamps at the bottom count the minutes. The first of these rows has 11 lamps, the second 4.
     In the first row every lamp represents 5 minutes.
     In this first row the 3rd, 6th and 9th lamp are red and indicate the first quarter, half and last quarter of an hour.
     The other lamps are yellow. In the last row with 4 lamps every lamp represents 1 minute.

     The lamps are switched on from left to right.

         Y = Yellow
         R = Red
         O = Off
     *
     */

    public String[] convertToBerlinTime(String time) {
        return null;
    }

    protected String getSeconds(int number) {
        return null;
    }

    protected String getTopHours(int number) {
        return null;
    }

    protected String getBottomHours(int number) {
        return null;
    }

    protected String getTopMinutes(int number) {
        return null;
    }

    protected String getBottomMinutes(int number) {
        return null;
    }



    /**************************************************************
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *                        ###   RESULT  ###
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *************************************************************/




    /*
    public String[] convertToBerlinTime(String time) {
        int[] parts = Arrays.asList(time.split(":")).stream().mapToInt(Integer::parseInt).toArray();
        return new String[] {
                getSeconds(parts[2]),
                getTopHours(parts[0]),
                getBottomHours(parts[0]),
                getTopMinutes(parts[1]),
                getBottomMinutes(parts[1])
        };
    }

    protected String getSeconds(int number) {
        if (number % 2 == 0) return "Y";
        else return "O";
    }

    protected String getTopHours(int number) {
        return getOnOff(4, getTopNumberOfOnSigns(number));
    }

    protected String getBottomHours(int number) {
        return getOnOff(4, number % 5);
    }

    protected String getTopMinutes(int number) {
        return getOnOff(11, getTopNumberOfOnSigns(number), "Y").replaceAll("YYY", "YYR");
    }

    protected String getBottomMinutes(int number) {
        return getOnOff(4, number % 5, "Y");
    }

    // Default value for onSign would be useful
    private String getOnOff(int lamps, int onSigns) {
        return getOnOff(lamps, onSigns, "R");
    }
    private String getOnOff(int lamps, int onSigns, String onSign) {
        String out = "";
        // String multiplication would be useful
        for (int i = 0; i < onSigns; i++) {
            out += onSign;
        }
        for (int i = 0; i < (lamps - onSigns); i++) {
            out += "O";
        }
        return out;
    }

    private int getTopNumberOfOnSigns(int number) {
        return (number - (number % 5)) / 5;
    }
     */
}
