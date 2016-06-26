package com.nicolasguignard.bowling.frame;

import java.util.List;

/**
 * Created by Nicolas Guignard on 24/06/2016.
 */
public class SpareFrame extends SimpleFrame {


    /**
     * First roll of next frame to add to the two other rolls
     */
    private Integer spareRoll;

    public SpareFrame(Integer first, Integer second) {
        super(first, second);
    }

    @Override
    public int getScore() {
        return super.getScore();
    }

    @Override
    public List<Integer> getRolls() {

        final List<Integer> rolls = super.getRolls();
        rolls.add(spareRoll);
        return rolls;
    }

    @Override
    public void roll(int roll) {
        spareRoll = Integer.valueOf(roll);
    }
}
