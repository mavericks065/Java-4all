package myKatas.ng.java8katas;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static myKatas.ng.java8katas.SumReduceFilterMapPartitioningGroupinJoining.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class SumReduceFilterMapPartitioningGroupinJoiningTest {

    @Test
    public void should_convert_collection_elements_toUpperCase() {
        // GIVEN
        List<Integer> numbers = asList(1, 2, 3, 4, 5);

        // WHEN
        Integer result = calculate(numbers);

        // THEN
        assertThat(result).isEqualTo(1 + 2 + 3 + 4 + 5);
    }

    @Test
    public void should_return_names_of_all_kids_from_norway() {
        // GIVEN
        Person sara = new Person("Sara", 4);
        Person viktor = new Person("Viktor", 40);
        Person eva = new Person("Eva", 42);
        Person anna = new Person("Anna", 5);
        List<Person> collection = asList(sara, eva, viktor, anna);

        // WHEN
        Set<String> result = getKidNames(collection);

        // THEN
        assertThat(result)
                .contains("Sara", "Anna")
                .doesNotContain("Viktor", "Eva");
    }

    @Test
    public void should_separate_kids_from_adults() {
        // GIVEN
        Person sara = new Person("Sara", 4);
        Person viktor = new Person("Viktor", 40);
        Person eva = new Person("Eva", 42);
        List<Person> collection = asList(sara, eva, viktor);

        // WHEN
        Map<Boolean, List<Person>> result = partitionAdults(collection);

        // THEN
        assertThat(result.get(true)).hasSameElementsAs(asList(viktor, eva));
        assertThat(result.get(false)).hasSameElementsAs(asList(sara));
    }

    @Test
    public void group_should_separate_kids_from_adults() {

        // GIVEN
        Person sara = new Person("Sara", 4, "Norwegian");
        Person viktor = new Person("Viktor", 40, "Serbian");
        Person eva = new Person("Eva", 42, "Norwegian");
        List<Person> collection = asList(sara, eva, viktor);

        // WHEN
        Map<String, List<Person>> result = groupByNationality(collection);

        // THEN
        assertThat(result.get("Norwegian")).hasSameElementsAs(asList(sara, eva));
        assertThat(result.get("Serbian")).hasSameElementsAs(asList(viktor));
    }

    @Test
    public void should_return_people_names_separated_By_comma() {

        // GIVEN
        Person sara = new Person("Sara", 4);
        Person viktor = new Person("Viktor", 40);
        Person eva = new Person("Eva", 42);
        List<Person> collection = asList(sara, viktor, eva);

        // WHEN
        String result = namesToString(collection);

        // THEN
        assertThat(result)
                .isEqualTo("Names: Sara, Viktor, Eva.");
    }


}
