package myKatas.ng.BerlinClockKata;

import org.junit.Assert;
import org.junit.Test;

public class BerlinClockTest {

    BerlinClock berlinClock = new BerlinClock();

    // Yellow lamp should blink on/off every two seconds
    @Test
    public void should_assert_yellow_lamp_blink_on_off_every_2_seconds() {
        Assert.assertEquals("Y", berlinClock.getSeconds(0));
        Assert.assertEquals("O", berlinClock.getSeconds(1));
        Assert.assertEquals("O", berlinClock.getSeconds(59));
    }

    // Top hours should have 4 lamps
    @Test
    public void should_assert_top_hours_have_4_lamps() {
        Assert.assertEquals(4, berlinClock.getTopHours(7).length());
    }

    // Top hours should light a red lamp for every 5 hours
    @Test
    public void should_assert_that_top_hours_should_light_red_lamp_for_every_5_hours() {
        Assert.assertEquals("OOOO", berlinClock.getTopHours(0));
        Assert.assertEquals("RROO", berlinClock.getTopHours(13));
        Assert.assertEquals("RRRR", berlinClock.getTopHours(23));
        Assert.assertEquals("RRRR", berlinClock.getTopHours(24));
    }

    // Bottom hours should have 4 lamps
    @Test
    public void should_test_bottom_hours_have_4_lamps() {
        Assert.assertEquals(4, berlinClock.getBottomHours(5).length());
    }

    // Bottom hours should light a red lamp for every hour left from top hours
    @Test
    public void should_test_bottom_hours_light_red_lamp_for_every_hour_left_from_top_hours() {
        Assert.assertEquals("OOOO", berlinClock.getBottomHours(0));
        Assert.assertEquals("RRRO", berlinClock.getBottomHours(13));
        Assert.assertEquals("RRRO", berlinClock.getBottomHours(23));
        Assert.assertEquals("RRRR", berlinClock.getBottomHours(24));
    }

    // Top minutes should have 11 lamps
    @Test
    public void should_verify_top_minutes_have_11_lamps() {
        Assert.assertEquals(11, berlinClock.getTopMinutes(34).length());
    }

    // Top minutes should have 3rd, 6th and 9th lamps in red to indicate first quarter, half and last quarter
    @Test
    public void should_testTopMinutesShouldHave3rd6thAnd9thLampsInRedToIndicateFirstQuarterHalfAndLastQuarter() {
        String minutes32 = berlinClock.getTopMinutes(32);
        Assert.assertEquals("R", minutes32.substring(2, 3));
        Assert.assertEquals("R", minutes32.substring(5, 6));
        Assert.assertEquals("O", minutes32.substring(8, 9));
    }

    // Top minutes should light a yellow lamp for every 5 minutes unless it's first quarter, half or last quarter
    @Test
    public void should_assert_that_top_minutes_light_yellow_lamp_for_every_5_minutes_unless_it_is_first_quarter_half_or_last_quarter() {
        Assert.assertEquals("OOOOOOOOOOO", berlinClock.getTopMinutes(0));
        Assert.assertEquals("YYROOOOOOOO", berlinClock.getTopMinutes(17));
        Assert.assertEquals("YYRYYRYYRYY", berlinClock.getTopMinutes(59));
    }

    // Bottom minutes should have 4 lamps
    @Test
    public void should_verify_bottom_minutes_have_4_lamps() {
        Assert.assertEquals(4, berlinClock.getBottomMinutes(0).length());
    }

    // Bottom minutes should light a yellow lamp for every minute left from top minutes
    @Test
    public void should_assert_bottom_minutes_light_yellow_lamp_for_every_minute_left_from_top_minutes() {
        Assert.assertEquals("OOOO", berlinClock.getBottomMinutes(0));
        Assert.assertEquals("YYOO", berlinClock.getBottomMinutes(17));
        Assert.assertEquals("YYYY", berlinClock.getBottomMinutes(59));
    }

    // Berlin Clock should result in array with 5 elements
    @Test
    public void should_verify_berlin_clock_result_in_array_with_5_elements()  {
        Assert.assertEquals(5, berlinClock.convertToBerlinTime("13:17:01").length);
    }

    // Berlin Clock it should "result in correct seconds, hours and minutes" in {
    @Test
    public void should_assert_berlin_clock_result_in_correct_seconds_hours_and_minutes() {
        String[] berlinTime = berlinClock.convertToBerlinTime("16:37:16");
        String[] expected = new String[] {"Y", "RRRO", "ROOO", "YYRYYRYOOOO", "YYOO"};
        Assert.assertEquals(expected.length, berlinTime.length);
        for (int index = 0; index < expected.length; index++) {
            Assert.assertEquals(expected[index], berlinTime[index]);
        }
    }

}