package fr.ng.bofJava9.overload;

import fr.ng.bofJava9.defaultMethod.Rocket;

import java.util.concurrent.Callable;

/**
 * Created by nicolasguignard on 27/11/2016.
 */
public interface Supplier<T extends Rocket> extends Callable<T> {

    T get();
}
