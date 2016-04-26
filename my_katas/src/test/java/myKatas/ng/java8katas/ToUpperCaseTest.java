package myKatas.ng.java8katas;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static myKatas.ng.java8katas.ToUpperCase.transform;
import static org.assertj.core.api.Assertions.assertThat;

/*
Convert elements of a collection to upper case.
 */
public class ToUpperCaseTest {

    @Test
    public void should_convert_collection_elements_toUpperCase() {
        // GIVEN
        List<String> collection = asList("My", "name", "is", "John", "Doe");
        List<String> expected = asList("MY", "NAME", "IS", "JOHN", "DOE");

        // WHEN
        final List<String> result = transform(collection);

        // THEN
        assertThat(result).hasSameElementsAs(expected);
    }

}