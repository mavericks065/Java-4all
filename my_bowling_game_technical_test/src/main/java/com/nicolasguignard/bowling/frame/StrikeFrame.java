package com.nicolasguignard.bowling.frame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas Guignard on 24/06/2016.
 */
public class StrikeFrame extends SimpleFrame {

    private List<Integer> nextRolls = new ArrayList<>();

    public StrikeFrame(Integer first) {
        super(first);
    }

    @Override
    public int getScore() {
        return getRolls().stream()
                              .filter(integer -> integer != null)
                              .reduce(0, (total, number) -> total + number);
    }

    @Override
    public List<Integer> getRolls() {
        final List<Integer> rolls = new ArrayList<>();
        final Integer firstRoll = getFirstRoll();

        if (firstRoll != null) {
            rolls.add(firstRoll);
        }
        rolls.addAll(nextRolls);
        return rolls;
    }

    @Override
    public void roll(int roll) {
        nextRolls.add(Integer.valueOf(roll));
    }
}
