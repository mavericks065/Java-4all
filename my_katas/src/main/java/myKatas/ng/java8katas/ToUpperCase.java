package myKatas.ng.java8katas;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ToUpperCase {

    /**
     * TODO
     */
    public static List<String> transform(List<String> collection) {

        return null;
    }





    /**************************************************************
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *                        ###   RESULT  ###
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *************************************************************/



    /*public static List<String> transform7(List<String> collection) {
        final List<String> coll = new ArrayList<>();
        for (final String element : collection) {
            coll.add(element.toUpperCase());
        }
        return coll;
    }

    public static List<String> transform(List<String> collection) {
        return collection.stream() // Convert collection to Stream
                .map(String::toUpperCase) // Convert each element to upper case
                .collect(Collectors.toList()); // Collect results to a new list
    }*/
}
