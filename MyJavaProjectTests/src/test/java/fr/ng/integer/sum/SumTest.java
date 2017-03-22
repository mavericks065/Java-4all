package fr.ng.integer.sum;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SumTest {
    private Sum s = new Sum();

    @Test
    public void test1() {
        assertEquals(-1, s.getSum(0, -1));
        assertEquals(1, s.getSum(0, 1));

    }

    @Test
    public void test2() {
        assertTrue(s.getSum(1, 0) == 1);
    }

    @Test
    public void test3() {
        assertTrue(s.getSum(1, 2) == 3);
    }

    @Test
    public void test4() {
        assertTrue(s.getSum(0, 1) == 1);
    }

    @Test
    public void test5() {
        assertTrue(s.getSum(1, 1) == 1);
    }

    @Test
    public void test6() {
        assertTrue(s.getSum(-1, 2) == 2);
    }

    @Test
    public void test7() {
        assertTrue(s.getSum(-1, 0) == -1);
    }
}
