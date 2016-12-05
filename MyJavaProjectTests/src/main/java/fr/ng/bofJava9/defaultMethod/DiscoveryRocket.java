package fr.ng.bofJava9.defaultMethod;

/**
 * Created by nicolasguignard on 28/11/2016.
 */
public class DiscoveryRocket implements AmericanRocket {

    @Override
    public void run(String command) {}

    @Override
    public boolean isEngineChecked() {
        return false;
    }

    @Override
    public boolean hasReachedMoon() {
        return false;
    }
}
