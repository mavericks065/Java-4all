package myKatas.ng.java8katas;

/**
 * Created by mba on 24/04/2016.
 */
public class Person {

    private String name;
    private String nationality;
    private int age;

    public Person(String name, int age) {

        this.name = name;
        this.age = age;
    }

    public Person(String name, int age, String nationality) {
        this.name = name;
        this.nationality = nationality;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
