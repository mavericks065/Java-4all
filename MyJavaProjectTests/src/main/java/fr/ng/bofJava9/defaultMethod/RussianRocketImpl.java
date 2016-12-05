package fr.ng.bofJava9.defaultMethod;

/**
 * Created by nicolasguignard on 27/11/2016.
 */
public class RussianRocketImpl implements RussianRocket {

    @Override
    public void run(String command) {}

    @Override
    public boolean isEngineChecked() {
        return false;
    }

    /* we are not forced to override it */
    @Override
    public void logEngineState() {
        System.out.println("let's send a dog in the space!");
    }
}
