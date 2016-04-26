package myKatas.ng.java8katas;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.partitioningBy;

/**
 * Created by mba on 24/04/2016.
 */
public class SumReduceFilterMapPartitioningGroupinJoining {

    /**
     * TODO
     */
    public static int calculate(List<Integer> people) {
        return 0;
    }


    public static Set<String> getKidNames(List<Person> people) {
        return null; // Collect values to a Set
    }

    public static Map<Boolean, List<Person>> partitionAdults(List<Person> people) {
        return null;
    }

    public static Map<String, List<Person>> groupByNationality(List<Person> people) {
        return null;
    }

    public static String namesToString(List<Person> people) {
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
    public

    public static int calculate7(List<Integer> numbers) {
        int total = 0;
        for (final int number : numbers) {
            total += number;
        }
        return total;
    }

    public static int calculate(List<Integer> people) {
        return people.stream() // Convert collection to Stream
                .reduce(0, (total, number) -> total + number); // Sum elements with 0 as starting value
    }

    public static Set<String> getKidNames7(List<Person> people) {
        Set<String> kids = new HashSet<>();
        for (Person person : people) {
            if (person.getAge() < 18) {
                kids.add(person.getName());
            }
        }
        return kids;
    }

    public static Set<String> getKidNames(List<Person> people) {
        return people.stream()
                .filter(person -> person.getAge() < 18) // Filter kids (under age of 18)
                .map(Person::getName) // Map Person elements to names
                .collect(Collectors.toSet()); // Collect values to a Set
    }


    public static Map<Boolean, List<Person>> partitionAdults7(List<Person> people) {
        Map<Boolean, List<Person>> map = new HashMap<>();
        map.put(true, new ArrayList<>());
        map.put(false, new ArrayList<>());
        for (Person person : people) {
            map.get(person.getAge() >= 18).add(person);
        }
        return map;
    }

    public static Map<Boolean, List<Person>> partitionAdults(List<Person> people) {
        return people.stream() // Convert collection to Stream
                .collect(partitioningBy(p -> p.getAge() >= 18)); // Partition stream of people into adults (age => 18) and kids
    }

    public static Map<String, List<Person>> groupByNationality7(List<Person> people) {
        Map<String, List<Person>> map = new HashMap<>();
        for (Person person : people) {
            if (!map.containsKey(person.getNationality())) {
                map.put(person.getNationality(), new ArrayList<>());
            }
            map.get(person.getNationality()).add(person);
        }
        return map;
    }

    public static Map<String, List<Person>> groupByNationality(List<Person> people) {
        return people.stream() // Convert collection to Stream
                .collect(groupingBy(Person::getNationality)); // Group people by nationality
    }

    public static String namesToString7(List<Person> people) {
        String label = "Names: ";
        StringBuilder sb = new StringBuilder(label);
        for (Person person : people) {
            if (sb.length() > label.length()) {
                sb.append(", ");
            }
            sb.append(person.getName());
        }
        sb.append(".");
        return sb.toString();
    }

    public static String namesToString(List<Person> people) {
        return people.stream() // Convert collection to Stream
                .map(Person::getName) // Map Person to name
                .collect(joining(", ", "Names: ", ".")); // Join names
    }*/


}
