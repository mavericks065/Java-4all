package fr.ng.bofJava9.overload;

import fr.ng.bofJava9.defaultMethod.AmericanRocket;
import fr.ng.bofJava9.defaultMethod.Rocket;

import java.util.concurrent.Callable;

/**
 * Created by nicolasguignard on 27/11/2016.
 */
public class MaintainRocket {

    static <T> T run(Callable<T> c) throws Exception {
        return c.call();
    }

    static <T extends Rocket> T run(Supplier<T> s) throws Exception {
        return s.get();
    }
}
