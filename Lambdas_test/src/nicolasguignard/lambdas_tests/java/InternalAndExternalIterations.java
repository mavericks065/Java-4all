package nicolasguignard.lambdas_tests.java;

import nicolasguignard.lambdas_tests.java.models.Shape;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class InternalAndExternalIterations {

    public static void main(String[] args) {

        List<Shape> shapes = null;

        simpleIterationsExamples();

        System.out.println("###########################");
        System.out.println("Second test");
        System.out.println("###########################");
        try {
            shapes.forEach(s -> s.setSurface(10));
        } catch (NullPointerException e) {
            System.out.println("If my colection is null forEach throws a null pointer exception");
        }

    }

    private static void simpleIterationsExamples() {

        System.out.println("###########################");
        System.out.println("first test");
        System.out.println("###########################");
        // Creation of an ArrayList of 100 000 000 shapes
        List<Shape> shapes = new ArrayList<>(10000000);
        for (int i = 0; i < 10000000; i++) {
            shapes.add(new Shape());
        }

        final Long start = System.currentTimeMillis();

        for (Shape shape : shapes) {
            shape.setColor(Shape.Color.INDIGO);
        }

        final Long end = System.currentTimeMillis();
        System.out.println("duration with old for each : ");
        System.out.println(end - start + " ms");

        final Long start1 = System.currentTimeMillis();

        shapes.forEach(s -> s.setColor(Shape.Color.GREEN));

        final Long end1 = System.currentTimeMillis();
        System.out.println("duration with new each : ");
        System.out.println(end1 - start1 + " ms");
    }

}
