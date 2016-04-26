package myKatas.ng.java8katas;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static myKatas.ng.java8katas.OldestPerson.getOldestPerson;
import static org.assertj.core.api.Assertions.assertThat;

/*
Get oldest person from the collection
 */
public class OldestPersonTest {

    @Test
    public void should_return_oldest_person() {
        // GIVEN
        Person sara = new Person("Sara", 4);
        Person viktor = new Person("Viktor", 40);
        Person eva = new Person("Eva", 42);
        List<Person> collection = asList(sara, eva, viktor);

        // WHEN
        final Person result = getOldestPerson(collection);

        // THEN
        assertThat(result).isEqualToComparingFieldByField(eva);
    }

}