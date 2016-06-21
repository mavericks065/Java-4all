package fr.ng.strings;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class StringTokens {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String s = scan.nextLine();
        scan.close();

        // Write your code here.
        s = s.trim();
        if (s.length() == 0) {
            System.out.println(0);

        } else if (s.length() <= 400000) {

            String[] result = s.split("[ !,?._'@]+");
            List<String> resultList = Arrays.asList(result);
            System.out.println(resultList.size());
            for(String str : resultList) {

                System.out.println(str);
            }

        }

    }

    /**
     * Exercise
     *
     * Given a string, , matching the regular expression [A-Za-z !,?._'@]+, split the string into tokens. We define a token to be one or more consecutive English alphabetic letters. Then, print the number of tokens, followed by each token on a new line.

     Input Format

     A single string, .

     Constraints

     is composed of English alphabetic letters, blank spaces, and any of the following characters: !,?._'@
     Output Format

     On the first line, print an integer, , denoting the number of tokens in string  (they do not need to be unique). Next, print each of the  tokens on a new line in the same order as they appear in input string .

     Sample Input

     He is a very very good boy, isn't he?
     Sample Output

     10
     He
     is
     a
     very
     very
     good
     boy
     isn
     t
     he
     Explanation

     We consider a token to be a contiguous segment of alphabetic characters. There are a total of  such tokens in string , and each token is printed in the same order in which it appears in string .
     */

}
