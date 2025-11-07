package com.daalitoy.apps.keedoh.data.model;

import com.daalitoy.apps.keedoh.data.model.impl.MsgLenIndicator;
import com.daalitoy.apps.keedoh.data.model.impl.ShortMliExclusive;
import com.daalitoy.apps.keedoh.data.model.impl.ShortMliInclusive;

public enum MLI {
  MLI_2I,
  MLI_2E;

  /*  private String name;
    private MsgLenIndicator mliImpl;

    public MLI(String name, MsgLenIndicator obj) {
      this.name = name;
      this.mliImpl = obj;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  */
  public MsgLenIndicator getMliImpl() {
    if (name().equals("MLI_2I")) {
      return new ShortMliInclusive();
    } else if (name().equals("MLI_2E")) {
      return new ShortMliExclusive();
    }
    throw new IllegalArgumentException();
  }

  public String toString() {
    return (name());
  }
}
