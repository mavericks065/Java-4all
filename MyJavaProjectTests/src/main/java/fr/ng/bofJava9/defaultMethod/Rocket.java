package fr.ng.bofJava9.defaultMethod;

/**
 * Created by nicolasguignard on 27/11/2016.
 */
public interface Rocket {

    void run(String command);

    boolean isEngineChecked();

    default void logEngineState() {
        System.out.println("Engine has been checked" + isEngineChecked());
    }

    static Rocket getRocket(String rocket) {
        if (rocket.equals("discovery")) {
            return new DiscoveryRocket();
        }

        return new RussianRocketImpl();
    }
}
