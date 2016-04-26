package myKatas.ng.FizzBuzzKata.solution;

import myKatas.ng.FizzBuzzKata.FizzBuzz;
import org.junit.Assert;
import org.junit.Test;

public class FizzBuzzTest {

    @Test
    public void should_return_fizz_if_the_number_is_dividable_by_3() {
        Assert.assertEquals("fizz", FizzBuzz.getResult(3));
    }

    @Test
    public void should_return_buzz_if_the_number_is_dividable_by_5() {
        Assert.assertEquals("buzz", FizzBuzz.getResult(5));
        Assert.assertEquals("buzz", FizzBuzz.getResult(10));
    }

    @Test
    public void should_return_buzz_if_the_number_is_dividable_by_15() {
        Assert.assertEquals("fizzbuzz", FizzBuzz.getResult(15));
        Assert.assertEquals("fizzbuzz", FizzBuzz.getResult(30));
    }

    @Test
    public void should_return_the_same_number_if_no_other_requirement_is_fulfilled() {
        Assert.assertEquals("1", FizzBuzz.getResult(1));
        Assert.assertEquals("2", FizzBuzz.getResult(2));
        Assert.assertEquals("4", FizzBuzz.getResult(4));
    }

}
