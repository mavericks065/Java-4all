package myKatas.ng.java8katas;

import java.util.List;
import java.util.stream.Collectors;

public class FilterCollection {

    /**
     * TODO Filter elements with length smaller than 4 characters
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
        List<String> newCollection = new ArrayList<>();
        for (String element : collection) {
            if (element.length() < 4) {
                newCollection.add(element);
            }
        }
        return newCollection;
    }

    public static List<String> transform(List<String> collection) {
        return collection.stream() // Convert collection to Stream
                .filter(value -> value.length() < 4) // Filter elements with length smaller than 4 characters
                .collect(toList()); // Collect results to a new list
    }*/

}
