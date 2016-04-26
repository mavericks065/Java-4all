package myKatas.ng.StringCalculator.solution;

import myKatas.ng.StringCalculator.solution.exceptions.NegativeNumbersException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringCalculatorTest {

    StringCalculator calc;

    @Before
    public void setupTest() {
        calc = new StringCalculator();
    }

    @Test
    public void should_return_0_on_empty_string() {
        int result = calc.add("");
        assertEquals(0, result);
    }

    @Test
    public void should_return_sum_of_1_parameter() {
        int result = calc.add("1");
        assertEquals(1, result);
    }

    @Test
    public void should_return_sum_of_2_parameters() {
        int result = calc.add("1,2");
        assertEquals(3, result);
    }

    @Test
    public void should_return_sum_of_3_parameters() {
        int result = calc.add("1,2,3");
        assertEquals(6, result);
    }

    @Test
    public void should_return_sum_of_multiple_parameters() {
        int result = calc.add("1,2,3,4,5,6");
        assertEquals(21, result);
    }

    @Test
    public void should_support_new_lines_and_of_commas() {
        int result = calc.add("1\n2,3");
        assertEquals(6, result);
    }

    @Test
    public void should_supportCustom_delimiters() {
        int result = calc.add("//;\n1;2;3");
        assertEquals(6, result);
    }

    @Test(expected = NegativeNumbersException.class)
    public void should_throw_exception_if_1_negative_number_is_passed() {
        calc.add("-1");
    }

    @Test(expected = NegativeNumbersException.class)
    public void should_throw_exception_if_multiple_negative_numbers_are_passed() {
        calc.add("-1,-1,1");
    }

    @Test
    public void shouldIgnoreNumbersBiggerThanThousand() {
        int result = calc.add("1,1001");
        assertEquals(1, result);

        result = calc.add("1,1000");
        assertEquals(1001, result);
    }

    @Test
    public void should_support_custom_delimiters_of_any_length() {
        int result = calc.add("//[---]\n1---2---3");
        assertEquals(6, result);
    }

    @Test
    public void should_escape_regex_meta_char_delimiters() {
        int result = calc.add(("//.\n1.2.3"));
        assertEquals(6, result);

        result = calc.add("//[***]\n1***2***3");
        assertEquals(6, result);
    }

    @Test
    public void should_support_multiple_custom_delimiters_of_any_length() {
        int result = calc.add("//[..][--]\n1..2--3");
        assertEquals(6, result);
    }

    @Test
    public void should_support_multiple_same_custom_delimiters_of_any_length() {
        int result = calc.add("//[..][..]\n1..2..3");
        assertEquals(6, result);
    }

    @Test
    public void should_be_able_to_set_single_delimiter_by_method() {
        StringCalculator sc = new StringCalculator();
        sc.setDelimiters(":");
        int result = sc.add("1:2:3");
        assertEquals(6, result);
    }

    @Test
    public void should_be_able_to_set_multiple_delimiters_by_method() {
        StringCalculator sc = new StringCalculator();
        sc.setDelimiters("[..][--]");
        int result = sc.add("1..2--3");
        assertEquals(6, result);
    }

}