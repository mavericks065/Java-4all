package com.nicolasguignard.bowling.game;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Nicolas Guignard on 24/06/2016.
 */
public class BowlingGameTest {

    @Test
    public void should_hit_the_gutter_and_return_score_of_0() throws Exception {
        // GIVEN
        BowlingGame bowlingGame = new BowlingGameImpl();

        // WHEN
        bowlingGame.roll(0); // hit the gutter

        // THEN
        Assert.assertEquals(0, bowlingGame.score());

    }

    @Test
    public void should_roll_a_number_of_pins_less_than_10_and_return_score_for_1_roll() throws Exception {
        // GIVEN
        BowlingGame bowlingGame = new BowlingGameImpl();

        // WHEN
        bowlingGame.roll(3);

        // THEN
        Assert.assertEquals(3, bowlingGame.score());
    }

    @Test
    public void should_roll_a_number_of_pins_less_than_10_and_return_score_for_2_rolls() throws Exception {
        // GIVEN
        BowlingGame bowlingGame = new BowlingGameImpl();

        // WHEN
        bowlingGame.roll(3);
        bowlingGame.roll(6);

        // THEN
        Assert.assertEquals(9, bowlingGame.score());
    }

    @Test
    public void should_roll_a_number_of_pins_less_than_10_and_return_score_for_3_rolls() throws Exception {
        // GIVEN
        BowlingGame bowlingGame = new BowlingGameImpl();

        // WHEN
        bowlingGame.roll(3);
        bowlingGame.roll(6);
        bowlingGame.roll(4);

        // THEN
        Assert.assertEquals(13, bowlingGame.score());
    }

    @Test
    public void should_roll_a_number_of_pins_less_than_10_and_return_score_for_a_whole_game() throws Exception {
        // GIVEN
        BowlingGame bowlingGame = new BowlingGameImpl();

        // WHEN
        bowlingGame.roll(3);
        bowlingGame.roll(6);
        bowlingGame.roll(4);
        bowlingGame.roll(3);
        bowlingGame.roll(6);
        bowlingGame.roll(0);
        bowlingGame.roll(2);
        bowlingGame.roll(4);
        bowlingGame.roll(1);
        bowlingGame.roll(6);
        bowlingGame.roll(3);
        bowlingGame.roll(6);
        bowlingGame.roll(4);
        bowlingGame.roll(3);
        bowlingGame.roll(6);
        bowlingGame.roll(1);
        bowlingGame.roll(2);
        bowlingGame.roll(4);
        bowlingGame.roll(1);
        bowlingGame.roll(6);

        // 3 + 6 +4+3+6+0+2+4+1+6+3+6+4+3+6+1+2+4+1+6
        // THEN
        Assert.assertEquals(71, bowlingGame.score());
    }

    @Test
    public void should_return_max_score_if_just_strikes() throws Exception {
        // GIVEN
        BowlingGame bowlingGame = new BowlingGameImpl();

        // WHEN
        bowlingGame.roll(10);
        bowlingGame.roll(10);
        bowlingGame.roll(10);
        bowlingGame.roll(10);
        bowlingGame.roll(10);
        bowlingGame.roll(10);
        bowlingGame.roll(10);
        bowlingGame.roll(10);
        bowlingGame.roll(10);
        bowlingGame.roll(10);

        // (3+6)+(4+6+6)+(6+1)+(2+4)+(1+6)+(6+1)+(2+4)+(1+6)+(2+8+2)+(2+4)

        // THEN
        Assert.assertEquals(300, bowlingGame.score());
    }

    @Test
    public void should_roll_spare_and_normal_rolls() throws Exception {
        // GIVEN
        BowlingGame bowlingGame = new BowlingGameImpl(10);

        // WHEN
        bowlingGame.roll(3);
        bowlingGame.roll(6);
        bowlingGame.roll(4);
        bowlingGame.roll(6); // Spare
        bowlingGame.roll(6);
        bowlingGame.roll(1);
        bowlingGame.roll(2);
        bowlingGame.roll(4);
        bowlingGame.roll(1);
        bowlingGame.roll(6);
        bowlingGame.roll(6);
        bowlingGame.roll(1);
        bowlingGame.roll(2);
        bowlingGame.roll(4);
        bowlingGame.roll(2);
        bowlingGame.roll(8); // Spare
        bowlingGame.roll(2);
        bowlingGame.roll(7);
        bowlingGame.roll(1);
        bowlingGame.roll(6);

        // (3+6)+(4+6+6)+(6+1)+(2+4)+(1+6)+(6+1)+(2+4)+(2+8+2)+(2+7)+(1+6)

        // THEN
        Assert.assertEquals(86, bowlingGame.score());
    }

    @Test
    public void should_roll_strike_spares_and_normal_rolls() throws Exception {
        // GIVEN
        BowlingGame bowlingGame = new BowlingGameImpl(10);

        // WHEN
        bowlingGame.roll(3);
        bowlingGame.roll(6);
        bowlingGame.roll(4);
        bowlingGame.roll(6); // Spare
        bowlingGame.roll(6);
        bowlingGame.roll(1);
        bowlingGame.roll(10); // Strike
        bowlingGame.roll(1);
        bowlingGame.roll(6);
        bowlingGame.roll(10); // Strike
        bowlingGame.roll(2);
        bowlingGame.roll(4);
        bowlingGame.roll(2);
        bowlingGame.roll(8); // Spare
        bowlingGame.roll(2);
        bowlingGame.roll(7);
        bowlingGame.roll(1);
        bowlingGame.roll(6);

        // (3+6)+(4+6+6)+(6+1)+(10+1+6)+(1+6)+(10+2+4)+(2+4)+(2+8+2)+(2+7)+(1+6)

        // THEN
        Assert.assertEquals(106, bowlingGame.score());
    }

    @Test
    public void should_play_normal_game_with_last_frame_as_a_spared() throws Exception {
        // GIVEN
        BowlingGame bowlingGame = new BowlingGameImpl(10);

        // WHEN
        bowlingGame.roll(3);
        bowlingGame.roll(6);
        bowlingGame.roll(4);
        bowlingGame.roll(6); // Spare
        bowlingGame.roll(6);
        bowlingGame.roll(1);
        bowlingGame.roll(2);
        bowlingGame.roll(4);
        bowlingGame.roll(1);
        bowlingGame.roll(6);
        bowlingGame.roll(6);
        bowlingGame.roll(1);
        bowlingGame.roll(2);
        bowlingGame.roll(4);
        bowlingGame.roll(2);
        bowlingGame.roll(8); // Spare
        bowlingGame.roll(2);
        bowlingGame.roll(7);
        bowlingGame.roll(4);
        bowlingGame.roll(6); // Spare
        bowlingGame.roll(5);

        // (3+6)+(4+6+6)+(6+1)+(2+4)+(1+6)+(6+1)+(2+4)+(2+8+2)+(2+7)+(4+6+5)

        // THEN
        Assert.assertEquals(94, bowlingGame.score());
    }

    @Test
    public void should_play_normal_game_with_last_frame_as_a_strike() throws Exception {
        // GIVEN
        BowlingGame bowlingGame = new BowlingGameImpl(10);

        // WHEN
        bowlingGame.roll(3);
        bowlingGame.roll(6);
        bowlingGame.roll(4);
        bowlingGame.roll(6); // Spare
        bowlingGame.roll(6);
        bowlingGame.roll(1);
        bowlingGame.roll(2);
        bowlingGame.roll(4);
        bowlingGame.roll(1);
        bowlingGame.roll(6);
        bowlingGame.roll(6);
        bowlingGame.roll(1);
        bowlingGame.roll(2);
        bowlingGame.roll(4);
        bowlingGame.roll(2);
        bowlingGame.roll(8); // Spare
        bowlingGame.roll(2);
        bowlingGame.roll(7);
        bowlingGame.roll(10); // Strike
        bowlingGame.roll(3);
        bowlingGame.roll(5);

        // (3+6)+(4+6+6)+(6+1)+(2+4)+(1+6)+(6+1)+(2+4)+(2+8+2)+(2+7)+(10+3+5)

        // THEN
        Assert.assertEquals(97, bowlingGame.score());
    }

}
