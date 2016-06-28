package myKatas.ng.puzzle;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mba on 28/06/2016.
 */
public class PuzzleTest {

    @Test
    public void should_do_as_in_the_statement_example() throws Exception {
        // GIVEN
        int n = 5;
        int c = 2;

        // WHEN
        Double result = Puzzle.getResult(n, c);

        // THEN
        Assert.assertEquals(new Double("21"), result);

    }

    @Test
    public void should_check_this_work_on_a_bit_bigger_number() throws Exception {
        // GIVEN
        int n = 10;
        int c = 3;

        // WHEN
        Double result = Puzzle.getResult(n, c);

        // THEN
        Assert.assertEquals(new Double("1263"), result);

    }

    @Test
    public void should_check_it_finds_the_results_on_hundreds() throws Exception {
        // GIVEN
        int n = 100;
        int c = 10;

        // WHEN
        Double result = Puzzle.getResult(n, c);

        // THEN
        Assert.assertEquals(new Double(5.139462350906961E20), result);

    }

    /**
     * In this case we are blocked by the limitation of Double.
     */
    @Test
    public void should_check_this_work_on_a_very_bigger_number() throws Exception {
        // GIVEN
        int n = 1000000;
        int c = 200;

        // WHEN
        Double result = Puzzle.getResult(n, c);

        // THEN
        Assert.assertEquals(new Double("Infinity"), result);

    }
}
