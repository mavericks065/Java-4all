package myKatas.ng.java8katas;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static myKatas.ng.java8katas.FlatCollection.transform;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by mba on 24/04/2016.
 */
public class FlatCollectionTest {

    @Test
    public void should_flatten_collection() {
        List<List<String>> collection = asList(asList("Viktor", "Farcic"), asList("John", "Doe", "Third"));
        List<String> expected = asList("Viktor", "Farcic", "John", "Doe", "Third");
        assertThat(transform(collection)).hasSameElementsAs(expected);
    }


}
