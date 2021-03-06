package fr.ng.BigInteger;

import java.math.BigInteger;
import java.util.Scanner;

/**
 * Created by mba on 8/07/2016.
 */
public class BigIntegerPbHackerRank {
    /**
     * PROBLEM
     *
     * In this problem, you have to add and multiply huge numbers! These numbers are so big that you can't contain them in any ordinary data types like a long integer.

     Use the power of Java's BigInteger class and solve this problem.

     Input Format

     There will be two lines containing two numbers,  and .

     Constraints

     and  are non-negative integers and can have maximum  digits.

     Output Format

     Output two lines. The first line should contain , and the second line should contain . Don't print any leading zeros.

     Sample Input

     1234
     20
     Sample Output

     1254
     224680
     Explanation




     */


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BigInteger A = sc.nextBigInteger();
        BigInteger B = sc.nextBigInteger();

        BigInteger resultAddition = A.add(B);
        BigInteger resultMultiply = A.multiply(B);

        System.out.println(resultAddition);
        System.out.println(resultMultiply);
    }
}
