package com.daalitoy.apps.keedoh.data.transit.ops;

import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.data.transit.FieldData;
import com.daalitoy.apps.keedoh.data.transit.MessageData;

public class FixedFieldStateChangeListener extends FieldStateChangeListener {

  private static FixedFieldStateChangeListener instance = new FixedFieldStateChangeListener();

  public static FieldStateChangeListener getInstance() {
    return (instance);
  }

  @Override
  public void fieldSelectionChanged(MessageData msgData, FieldData fieldData, boolean selected) {

    Field parent = fieldData.getField().getParent();
    if (parent != null) {
      if (BITMAP_CLASS_CHECK.apply(parent)) {
        // set the bit on for the bitmap
        if (selected) {
          msgData.getFieldData(parent).setOn(fieldData.getField().getSequence());
        } else {
          msgData.getFieldData(parent).setOff(fieldData.getField().getSequence());
        }
      }
    }
  }

  @Override
  public void fieldDataChanged(FieldData fieldData, boolean stringFlavour) {
    // TODO Auto-generated method stub

  }
}
