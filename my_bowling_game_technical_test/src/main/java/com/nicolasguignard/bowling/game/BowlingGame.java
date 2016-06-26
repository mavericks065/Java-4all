package com.nicolasguignard.bowling.game;

/**
 * Created by Nicolas Guignard on 24/06/2016.
 */
public interface BowlingGame {

    /**
     * add player's result of pins hit
     *
     * @param numberOfPins number of pins hit
     */
    void roll(int numberOfPins);

    /**
     * returns the score of the game
     *
     * @return
     */
    int score();
}
