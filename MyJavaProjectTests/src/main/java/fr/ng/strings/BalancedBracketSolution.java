package fr.ng.strings;

import java.util.LinkedList;

public class BalancedBracketSolution {
    public static final String openingBrackets = "({[<";
    public static final String closingBrackets = ")}]>";
    public static LinkedList<Character> stack;


    public static int hasBalancedBrackets(String str) {
        stack = new LinkedList<>();

        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {

            char c = chars[i];

            if (openingBrackets.indexOf(c) >= 0) {

                stack.push(c);

            } else if (closingBrackets.indexOf(c) >= 0) {

                if (stack.isEmpty()) {
                    return 0;
                }
                if (match(c)) {
                    stack.pop();
                } else {
                    return 0;
                }

            }
        }

        return stack.isEmpty() ? 1 : 0;
    }

    private static boolean match(Character closedBracket) {
        if (closedBracket.equals(')') && stack.peek().equals('(')) {
            return true;
        }
        if (closedBracket.equals('}') && stack.peek().equals('{')) {
            return true;
        }
        if (closedBracket.equals(']') && stack.peek().equals('[')) {
            return true;
        }
        if (closedBracket.equals('>') && stack.peek().equals('<')) {
            return true;
        }
        return false;
    }

    public static void main(String... args) {
        System.out.println(hasBalancedBrackets("[](){}<>"));
    }
}