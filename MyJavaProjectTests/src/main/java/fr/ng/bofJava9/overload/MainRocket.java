package fr.ng.bofJava9.overload;

import fr.ng.bofJava9.defaultMethod.DiscoveryRocket;

import java.util.concurrent.Callable;


/**
 * Created by nicolasguignard on 27/11/2016.
 */
public class MainRocket {

    public static void main(String[] args) throws Exception {

        // ambiguous:
        MaintainRocket.run(() -> null);  // which method do we call ?


        // less ambiguous? mouarf...

        MaintainRocket.run(new Supplier<DiscoveryRocket>() {
            @Override
            public DiscoveryRocket call() throws Exception {
                return null;
            }

            @Override
            public DiscoveryRocket get() {
                return null;
            }
        });

        MaintainRocket.run((Callable<Object>) (() -> null));

        MaintainRocket.run(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        });

    }
}
