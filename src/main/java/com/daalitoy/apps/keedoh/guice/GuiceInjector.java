package com.daalitoy.apps.keedoh.guice;

import com.daalitoy.apps.keedoh.guice.modules.KeedohDefaultModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class GuiceInjector {

    private static final Injector injector;

    static {
        injector = Guice.createInjector(new KeedohDefaultModule());
    }

    public static Injector getInjector() {
        return injector;
    }
}
