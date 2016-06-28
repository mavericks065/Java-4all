package myKatas.ng.puzzle;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Nicolas Guignard on 28/06/2016.
 */
public class Puzzle {


    public static Double getResult(int n, int c) {
        // create range of Integer : 1, 2, 3, ... n
        final List<Double> initialList = IntStream.rangeClosed(1, n).asDoubleStream().boxed().collect(Collectors.toList());

        Double result = initialList.stream()
                .map(integer -> myTruncatedFactorial(integer - 1, integer - c))
                .reduce(0.0, (total, number) -> total + number);

        return result;
    }

    private static double myTruncatedFactorial(double integer, double floor) {
        if (integer <= 0) {
            return 0;
        } else if(integer == 1) {
            return 1;
        } else if (integer == floor) {
            return floor;
        } else {
            return(integer*(myTruncatedFactorial(integer-1, floor)));
        }
    }
}
