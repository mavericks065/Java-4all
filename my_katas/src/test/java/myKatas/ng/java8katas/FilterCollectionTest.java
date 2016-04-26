package myKatas.ng.java8katas;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static myKatas.ng.java8katas.FilterCollection.transform;
import static org.assertj.core.api.Assertions.assertThat;

/*
Filter collection so that only elements with less then 4 characters are returned.
 */
public class FilterCollectionTest {

    @Test
    public void should_filter_collection() {
        // GIVEN
        List<String> collection = asList("My", "name", "is", "John", "Doe");
        List<String> expected = asList("My", "is", "Doe");

        // WHEN
        final List<String> result = transform(collection);

        // THEN
        assertThat(result).hasSameElementsAs(expected);
    }

}
