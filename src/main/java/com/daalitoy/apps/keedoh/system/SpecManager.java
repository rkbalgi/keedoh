package com.daalitoy.apps.keedoh.system;

import com.daalitoy.apps.keedoh.data.model.Spec;
import java.util.List;

public interface SpecManager {
  Spec newSpec(String specName);

  List<Spec> allSpecs();
}
