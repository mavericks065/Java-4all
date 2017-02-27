package fr.ng.strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StringCompareChallenge {

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String inputA = sc.next();
        String inputB = sc.next();
        int k = Integer.valueOf(inputB);

        List<String> subStrings = new ArrayList<>();

        for(int i = 0; i < inputA.length() - k + 1; i++) {
            String subString = inputA.substring(i, i+k);
            subStrings.add(subString);
        }
        subStrings.sort((a, b) -> a.compareTo(b));

        System.out.println(subStrings.toString());
        System.out.println(subStrings.get(0));
        System.out.println(subStrings.get(subStrings.size()-1));
    }

    /**
     * Exercise :
     *
     * Given a string, find out the lexicographically smallest and largest substring of length .

     [Note: Lexicographic order is also known as alphabetic order dictionary order. So "ball" is smaller than "cat", "dog" is smaller than "dorm". Capital letter always comes before smaller letter, so "Happy" is smaller than "happy" and "Zoo" is smaller than "ball".]

     Input Format

     First line will consist a string containing english alphabets which has at most  characters. 2nd line will consist an integer .

     Output Format

     In the first line print the lexicographically minimum substring. In the second line print the lexicographically maximum substring.

     Sample Input

     welcometojava
     3
     Sample Output

     ava
     wel
     Explanation

     Here is the list of all substrings of length :

     wel
     elc
     lco
     com
     ome
     met
     eto
     toj
     oja
     jav
     ava
     Among them ava is the smallest and wel is the largest.
     */
}
