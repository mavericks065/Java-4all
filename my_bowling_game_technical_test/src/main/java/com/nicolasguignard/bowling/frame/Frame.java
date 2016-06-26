package com.nicolasguignard.bowling.frame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas Guignard on 24/06/2016.
 */
public abstract class Frame {

    /**
     * Maximum number of pins in a frame.
     */
    public static final int NUMBER_OF_PINS = 10;

    /**
     * First roll, number of pins hit
     */
    private Integer first;

    /**
     * Second roll, number of pins hit
     */
    private Integer second;

    public Frame() {
    }

    public Frame(Integer first) {
        this.first = first;
    }

    public Frame(Integer second, Integer first) {
        this.second = second;
        this.first = first;
    }

    /**
     * return the sum of the rolls
     *
     * @return
     */
    public int getScore() {
        int score = getRolls().stream()
                .filter(integer -> integer != null)
                .reduce(0, (total, number) -> total + number);
        return score;
    }

    /**
     * return the list of rolls
     *
     * @return
     */
    public List<Integer> getRolls() {
        final List<Integer> rolls = new ArrayList<>();
        if (first != null) {
            rolls.add(first);
        }
        if (second != null) {
            rolls.add(second);
        }
        return rolls;
    }

    public abstract void roll(int roll);

    public Integer getFirstRoll() {
        return first;
    }

    public void setFirstRoll(Integer first) {
        this.first = first;
    }

    public Integer getSecondRoll() {
        return second;
    }

    public void setSecondRoll(Integer second) {
        this.second = second;
    }

    public abstract Frame getPredecessor();

    public abstract void setPredecessor(Frame predecessor);
}
