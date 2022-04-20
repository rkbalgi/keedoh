package com.daalitoy.apps.keedoh.system;



import io.github.rkbalgi.iso4k.Spec;

import java.util.List;

public interface SpecManager {
    Spec newSpec(String specName);

    List<Spec> allSpecs();
}
