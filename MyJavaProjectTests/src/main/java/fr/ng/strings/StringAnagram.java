package fr.ng.strings;

import java.util.Arrays;
import java.util.Scanner;

public class StringAnagram {

    static boolean isAnagram(String a, String b) {
        //Complete the function

        char [] arrayA = a.toLowerCase().toCharArray();
        char [] arrayB = b.toLowerCase().toCharArray();
        Arrays.sort(arrayA);
        Arrays.sort(arrayB);
        return Arrays.equals(arrayA, arrayB);

    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String A = sc.next();
        String B = sc.next();
        sc.close();
        boolean ret = isAnagram(A, B);
        if (ret) {
            System.out.println("Anagrams");
        } else {
            System.out.println("Not Anagrams");
        }

    }

    /**
     *
     * Exercise
     *
     * Two strings  and  are called anagrams if they consist same characters, but may be in different orders. So the list of anagrams of CAT is "CAT", "ACT" , "TAC", "TCA" ,"ATC" and "CTA".

     Given two strings, print "Anagrams" if they are anagrams, print "Not Anagrams" if they are not. The strings may consist at most 50 English characters; the comparison should NOT be case sensitive.

     This exercise will verify that you can sort the characters of a string, or compare frequencies of characters.

     Sample Input 1

     anagram
     margana
     Sample Output 1:

     Anagrams
     Sample Input 2

     anagramm
     marganaa
     Sample Output 2:

     Not Anagrams
     *
     */
}
