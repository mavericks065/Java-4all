package com.nicolasguignard.bowling.game;

import com.nicolasguignard.bowling.frame.*;

import java.util.LinkedList;

/**
 * Created by Nicolas Guignard on 24/06/2016.
 */
public class BowlingGameImpl implements BowlingGame {

    private final int NUMBER_OF_FRAMES;

    private final LinkedList<Frame> frames;

    private static final int MAXIMUM_SCORE = 300;

    public BowlingGameImpl() {
        NUMBER_OF_FRAMES = 10;
        frames = new LinkedList<>();
    }

    public BowlingGameImpl(int maxNumberOfFrames) {
        NUMBER_OF_FRAMES = maxNumberOfFrames;
        frames = new LinkedList<>();
    }

    @Override
    public void roll(int numberOfPins) {

        boolean hasToCreateNewFrame = frames.size() == 0
                || (frames.size() > 0 && frames.getLast().getScore() == 10)
                || (frames.size() > 0 && frames.getLast().getScore() < 10
                        && frames.getLast().getRolls().size() == 2);

        final Frame frame;

        if (hasToCreateNewFrame && !isLastFrame()) {

            frame = new SimpleFrame();

        } else if (isLastFrame()) {

            frame = buildLastFrame();

        } else {

            frame = frames.pollLast();
        }

        if (wasSpareFrame()) {

            convertPreviousFrameToSpareFrame(numberOfPins, frame);

        } else if (wasStrikeFrame()) {

            convertPreviousFrameToStrikeFrame(numberOfPins, frame);

        }

        frame.roll(numberOfPins);

        frames.add(frame);
    }

    @Override
    public int score() {

        // if the user scores only strikes he gets the maximum score
        if (frames.size() == NUMBER_OF_FRAMES
                && frames.stream().map(Frame::getFirstRoll).map(Integer::intValue).allMatch(i -> i == Frame.NUMBER_OF_PINS)) {

            return MAXIMUM_SCORE;

        } else {
            return frames.stream()
                            .map(Frame::getScore)
                            .reduce(0, (total, number) -> total + number);
        }
    }

    /**
     * to know if we are at the last frame of the game
     * @return
     */
    private boolean isLastFrame() {
        return frames.size() == NUMBER_OF_FRAMES;
    }

    /**
     * method that tells us if the previous frame was a SpareFrame to be able
     * to convert it.
     *
     * @return
     */
    private boolean wasSpareFrame() {
        // to have a spare frame it must exist so frames must have elements and
        // the score of the previous frame must be equal to the number of pins
        // in two bowls
        return frames.size() > 0
                && frames.getLast().getScore() == Frame.NUMBER_OF_PINS
                && frames.getLast().getRolls().size() == 2;
    }

    /**
     * method that tells us if the previous frame was a StrikeFrame to be able
     * to convert it
     *
     * @return
     */
    private boolean wasStrikeFrame() {
        return frames.size() > 0
                && (frames.getLast().getFirstRoll() == Frame.NUMBER_OF_PINS
                        || (frames.getLast().getPredecessor() != null
                                && frames.getLast().getPredecessor().getFirstRoll() == Frame.NUMBER_OF_PINS));
    }

    /**
     * depending of the last roll we get or we create the last frame.
     *
     * @return
     */
    private Frame buildLastFrame() {
        final Frame predecessor = frames.pollLast();
        if (predecessor instanceof LastFrame) {
            return predecessor;
        } else {

            return new LastFrame(predecessor.getFirstRoll());
        }
    }

    private void convertPreviousFrameToStrikeFrame(int numberOfPins, Frame frame) {
        final StrikeFrame strikeFrame;

        // if the new frame still did not reference that its rolls must be added to the strike frame
        if (frame.getPredecessor() == null) {

            final SimpleFrame predecessor = (SimpleFrame) frames.pollLast();

            strikeFrame = new StrikeFrame(predecessor.getFirstRoll());
            strikeFrame.roll(numberOfPins);
            frame.setPredecessor(strikeFrame);
            frames.add(strikeFrame);
        } else {

            strikeFrame = (StrikeFrame) frame.getPredecessor();
            strikeFrame.roll(numberOfPins);
            frame.setPredecessor(null);
            frames.set(frames.size() - 1, strikeFrame);
        }
    }

    private void convertPreviousFrameToSpareFrame(int numberOfPins, Frame frame) {
        final SimpleFrame predecessor = (SimpleFrame) frames.pollLast();

        final SpareFrame spareFrame = new SpareFrame(predecessor.getFirstRoll(), predecessor.getSecondRoll());
        spareFrame.roll(numberOfPins);

        frames.add(spareFrame);
    }
}
