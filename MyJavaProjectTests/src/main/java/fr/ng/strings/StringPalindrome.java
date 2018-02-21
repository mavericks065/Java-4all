package fr.ng.strings;

import java.util.Scanner;

public class StringPalindrome {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner sc=new Scanner(System.in);
        String inputA = sc.next();
        StringBuilder sb = new StringBuilder(inputA).reverse();

        if (inputA.equals(sb.toString())) {
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }
    }

    /**
     * Exercise :
     *
     * A palindrome is a word, phrase, number, or other sequence of characters which reads the same backward or forward.(Wikipedia)
     Given a string , print "Yes" if it is a palindrome, print "No" otherwise. The strings will consist lower case english letters only. The strings will have at most 50 characters.

     Some examples of palindromes are "madam", "anna", "reviver".

     Sample Input

     madam
     Sample Output

     Yes
     */
}
