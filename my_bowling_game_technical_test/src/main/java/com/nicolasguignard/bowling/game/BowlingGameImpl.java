package com.nicolasguignard.bowling.game;

import com.nicolasguignard.bowling.frame.*;

import java.util.LinkedList;

/**
 * Created by Nicolas Guignard on 24/06/2016.
 */
public class BowlingGameImpl implements BowlingGame {

    private final int numberOfFrames;
    private final int maximumScore;

    private final LinkedList<Frame> frames;

    public BowlingGameImpl() {
        numberOfFrames = 10;
        maximumScore = 300;
        frames = new LinkedList<>();
    }

    public BowlingGameImpl(int maxNumberOfFrames) {
        numberOfFrames = maxNumberOfFrames;
        maximumScore = (maxNumberOfFrames * 20) * 3 /2;
        frames = new LinkedList<>();
    }

    @Override
    public void roll(int numberOfPins) {

        boolean hasToCreateNewFrame = frames.size() == 0
                || (frames.size() > 0 && frames.getLast().getScore() == 10)
                || (frames.size() > 0 && frames.getLast().getScore() < 10
                        && frames.getLast().getRolls().size() == 2);

        final Frame frame = getFrame(numberOfPins, hasToCreateNewFrame);

        frame.roll(numberOfPins);

        frames.add(frame);
    }

    @Override
    public int score() {

        // if the user scores only strikes he gets the maximum score
        if (frames.size() == numberOfFrames
                && frames.stream().map(Frame::getFirstRoll).map(Integer::intValue).allMatch(i -> i == Frame.NUMBER_OF_PINS)) {

            return maximumScore;

        } else {
            return frames.stream()
                            .map(Frame::getScore)
                            .reduce(0, (total, number) -> total + number);
        }
    }

    private Frame getFrame(final int numberOfPins, final boolean hasToCreateNewFrame) {
        final Frame frame;
        if (hasToCreateNewFrame && !isLastFrame()) {

            frame = new SimpleFrame();

        } else if (isLastFrame()) {

            frame = buildLastFrame();

        } else {

            frame = frames.pollLast();
        }

        if (wasSpareFrame()) {

            convertPreviousFrameToSpareFrame(numberOfPins);

        } else if (wasStrikeFrame()) {

            convertPreviousFrameToStrikeFrame(numberOfPins, frame);

        }
        return frame;
    }

    /**
     * to know if we are at the last frame of the game
     * @return
     */
    private boolean isLastFrame() {
        return frames.size() == numberOfFrames;
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

    /**
     * Replace the previous frame by a strike frame
     *
     * @param numberOfPins
     * @param frame
     */
    private void convertPreviousFrameToStrikeFrame(int numberOfPins, Frame frame) {
        final StrikeFrame strikeFrame;

        // if the new frame still did not reference that its rolls must be added
        // to the strike frame we poll the last frame and make it a StrikeFrame
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

    /**
     * Replace the previous frame by a spare frame
     *
     * @param numberOfPins
     */
    private void convertPreviousFrameToSpareFrame(int numberOfPins) {
        final SimpleFrame predecessor = (SimpleFrame) frames.pollLast();

        final SpareFrame spareFrame = new SpareFrame(predecessor.getFirstRoll(), predecessor.getSecondRoll());
        spareFrame.roll(numberOfPins);

        frames.add(spareFrame);
    }
}
