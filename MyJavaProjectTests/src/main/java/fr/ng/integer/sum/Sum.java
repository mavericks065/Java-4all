package fr.ng.integer.sum;


/**
 * Exercise
 *
 GetSum(1, 0) == 1   // 1 + 0 = 1
 GetSum(1, 2) == 3   // 1 + 2 = 3
 GetSum(0, 1) == 1   // 0 + 1 = 1
 GetSum(1, 1) == 1   // 1 Since both are same
 GetSum(-1, 0) == -1 // -1 + 0 = -1
 GetSum(-1, 2) == 2  // -1 + 0 + 1 + 2 = 2
 */
public class Sum {
    public int getSum(int a, int b) {
        int min = Math.min(a, b);
        int max = Math.max(a, b);
        int result = 0;
        while (min <= max) {
            result += min;
            min++;
        }
        return result;
    }
}