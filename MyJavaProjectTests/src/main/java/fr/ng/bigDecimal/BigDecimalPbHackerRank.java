package fr.ng.bigDecimal;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by mba on 8/07/2016.
 */
public class BigDecimalPbHackerRank {

    public static void main(String[] argh) {

        /**
         * PROBLEM
         *
         * Java BigDecimal class can handle arbitrary-precision signed decimal numbers. Lets test your knowledge on them!

         You are given  real numbers, sort them in descending order! Read data from System.in.

         Note: To make the problem easier, we provided you the input/output part in the editor. Print the numbers as they appeared in the input, don't change anything. If two numbers represent numerically equivalent values, the output must list them in original order of the input.

         Input Format

         The first line will consist an integer , each of the next n lines will contain a real number.  will be at most 200. The numbers can have at most 300 digits!

         Output Format

         Print the numbers in descending orders, one number in each line.

         Sample Input

         9
         -100
         50
         0
         56.6
         90
         0.12
         .12
         02.34
         000.000
         Sample Output

         90
         56.6
         50
         02.34
         0.12
         .12
         0
         000.000
         -100
         */
        //Input

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
//        List<BigDecimal> bigDecimals = new ArrayList<>(n);
//        for(int i = 0; i < n; i++) {
//            bigDecimals.add(sc.nextBigDecimal()); OR
//            bigDecimals.add(new BigDecimal(sc.next()));
//        }

        String[] s = new String[n + 2];
        for (int i = 0; i < n; i++) {
            s[i] = sc.next();
        }
        // in case of using array of strings
        Arrays.sort(s, (s1, s2) -> {
            BigDecimal bd1 = new BigDecimal(s1);
            BigDecimal bd2 = new BigDecimal(s2);

            return bd2.compareTo(bd1);
        });
//        bigDecimals.stream().sorted(Comparator.reverseOrder()).forEach( bg -> System.out.println(bg));
        for (int i = 0; i < n; i++) {
            System.out.println(s[i]);
        }

    }
}
