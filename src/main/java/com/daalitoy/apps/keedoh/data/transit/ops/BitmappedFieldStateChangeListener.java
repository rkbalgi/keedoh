package com.daalitoy.apps.keedoh.data.transit.ops;

import com.daalitoy.apps.keedoh.data.transit.FieldData;
import com.daalitoy.apps.keedoh.data.transit.MessageData;

public class BitmappedFieldStateChangeListener extends FieldStateChangeListener {

  private static BitmappedFieldStateChangeListener instance =
      new BitmappedFieldStateChangeListener();

  public static FieldStateChangeListener getInstance() {
    return (instance);
  }

  @Override
  public void fieldSelectionChanged(MessageData msgData, FieldData fieldData, boolean selected) {
    // TODO Auto-generated method stub

  }

  @Override
  public void fieldDataChanged(FieldData fieldData, boolean stringFlavour) {
    // TODO Auto-generated method stub

  }
}
