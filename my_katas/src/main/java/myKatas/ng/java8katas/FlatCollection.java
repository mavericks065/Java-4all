package myKatas.ng.java8katas;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mba on 24/04/2016.
 */
public class FlatCollection {


    /**
     * TODO transform list of list
     */
    public static List<String> transform(List<List<String>> collection) {
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


    /*
    public static List<String> transform7(List<List<String>> collection) {
        List<String> newCollection = new ArrayList<>();
        for (List<String> subCollection : collection) {
            for (String value : subCollection) {
                newCollection.add(value);
            }
        }
        return newCollection;
    }

    public static List<String> transform(List<List<String>> collection) {
        return collection.stream() // Convert collection to Stream
                .flatMap(value -> value.stream()) // Replace list with stream
                .collect(toList()); // Collect results to a new list
    }*/
}
