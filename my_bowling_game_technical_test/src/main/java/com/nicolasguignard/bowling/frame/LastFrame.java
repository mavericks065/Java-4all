package com.nicolasguignard.bowling.frame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas Guignard on 24/06/2016.
 */
public class LastFrame extends Frame {

    /**
     * Spared and strike Rolls on the last frame
     */
    private List<Integer> bonusRolls = new ArrayList<>();

    public LastFrame(Integer first) {
        super(first);
    }

    @Override
    public int getScore() {
        return super.getScore();
    }

    @Override
    public List<Integer> getRolls() {
        final List<Integer> rolls = super.getRolls();
        rolls.addAll(bonusRolls);

        return rolls;
    }

    @Override
    public void roll(int roll) {
        if (getFirstRoll() == null) {
            setFirstRoll(Integer.valueOf(roll));
        } else if (getSecondRoll() == null){
            setSecondRoll(Integer.valueOf(roll));
        } else {
            bonusRolls.add(Integer.valueOf(roll));
        }
    }

    @Override
    public Frame getPredecessor() {
        // don't do anything
        return null;
    }

    @Override
    public void setPredecessor(Frame predecessor) {
        // don't do anything
    }
}
