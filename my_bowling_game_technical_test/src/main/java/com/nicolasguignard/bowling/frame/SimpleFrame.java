package com.nicolasguignard.bowling.frame;

import java.util.List;

/**
 * Created by Nicolas Guignard on 24/06/2016.
 */
public class SimpleFrame extends Frame {

    private SimpleFrame predecessor;

    public SimpleFrame() {
    }

    public SimpleFrame(Integer first) {
        super(first);
    }

    public SimpleFrame(Integer first, Integer second) {
        super(first, second);
    }

    @Override
    public int getScore() {
        return super.getScore();
    }

    @Override
    public List<Integer> getRolls() {
        return super.getRolls();
    }

    @Override
    public void roll(int roll) {
        if (getFirstRoll() == null) {
            setFirstRoll(Integer.valueOf(roll));
        } else {
            setSecondRoll(Integer.valueOf(roll));
        }
    }

    @Override
    public SimpleFrame getPredecessor() {
        return predecessor;
    }

    @Override
    public void setPredecessor(Frame predecessor) {
        this.predecessor = (SimpleFrame) predecessor;
    }
}
