package fr.ng.bofJava9.defaultMethod;

/**
 * Created by nicolasguignard on 27/11/2016.
 */
public interface RussianRocket extends Rocket {

    default void logEngineState() {
        System.out.println("We don't care of the people in the rocket");
    }


}
