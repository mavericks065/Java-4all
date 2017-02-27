package fr.ng.strings;

import java.util.Arrays;

public class TestStringStaircase {

    static void stairCase1(int n) {
        if (n < 1 || n > 100) {
            return;
        }
        StringBuilder sb;
        for (int i = n; i > 0; i--) {
            sb = new StringBuilder();

            for (int j = 1; j < i; j++) {
                sb.append(" ");
            }

            for (int k = i - 1; k < n; k++) {
                sb.append("#");
            }
            System.out.print(sb.toString());
            if (i - 1 > 0) {
                System.out.print("\n");
            }
        }
    }

    static void stairCase2(int n) {

        if (n < 1 || n > 100) {
            return;
        }

        StringBuilder sb;

        for (int i = 0; i < n; i++) {
            sb = new StringBuilder();

            for (int j = 0; j < n - i - 1; j++) {
                sb.append(" ");
            }

            for (int k = n - i - 1; k < n; k++) {
                sb.append("#");
            }
            System.out.print(sb.toString());
            if (i - 1 < n) {
                System.out.print("\n");
            }
        }
    }

    static int summation(int[] numbers) {
        if (numbers.length == 0 || numbers.length > 10000) {
            return 0;
        }
        int length = numbers[0];
        int[] newTab = Arrays.copyOfRange(numbers, 1, length + 1);
        int sum = 0;
        for (int i: newTab) {
            if (i > 0 && i < 10000) {
                sum+=i;
            }
        }
        return sum;
    }


    public static void main(String[] args) {
        stairCase1(6);
        System.out.println();
        stairCase2(6);
        int[] toSum = {5, 1, 2, 3, 4, 5};
        System.out.println(summation(toSum));
        int[] toSum2 = {2, 12, 12};
        System.out.println(summation(toSum2));
    }
}
