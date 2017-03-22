package fr.ng.strings.mumbling;


/**
 * Exercise :
 * This time no story, no theory. The examples below show you how to write function accum:

 Examples:

 Accumul.accum("abcd");    // "A-Bb-Ccc-Dddd"
 Accumul.accum("RqaEzty"); // "R-Qq-Aaa-Eeee-Zzzzz-Tttttt-Yyyyyyy"
 Accumul.accum("cwAt");    // "C-Ww-Aaa-Tttt"
 The parameter of accum is a string which includes only letters from a..z and A..Z.
 */
public class Accumul {

    public static String accum(String s) {
        // your code
        final StringBuilder sb = new StringBuilder();
        final char[] characters = s.toLowerCase().toCharArray();
        for (int i = 0; i <= characters.length - 1; i++) {
            sb.append(String.valueOf(characters[i]).toUpperCase());

            if (i > 0) {
                for (int j = 1; j <= i; j++) {
                    sb.append(String.valueOf(characters[i]));
                }
            }
            if (i != characters.length - 1) {
                sb.append("-");
            }
        }
        return sb.toString();
    }
}
