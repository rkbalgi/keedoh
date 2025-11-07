package com.daalitoy.apps.keedoh.data.fields.processor;

import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.data.model.FixedField;
import com.daalitoy.apps.keedoh.data.transit.FieldData;
import com.daalitoy.apps.keedoh.data.transit.MessageData;
import com.daalitoy.apps.keedoh.data.util.CharsetUtils;
import com.daalitoy.apps.keedoh.ui.util.Hex;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class FixedFieldProcessor implements FieldProcessor {

  @Override
  public void read(Field field, MessageData msgData, ByteBuffer buffer) {

    FixedField f = (FixedField) field;
    if (f.hasChildren()) {
      buffer.mark();
      byte[] tmp = new byte[field.getLength()];
      if (log.isDebugEnabled()) {
        log.debug(String.format("field: %s, field-data:%s", f.getFieldName(), Hex.toString(tmp)));
      }
      msgData.setFieldData(f, new FieldData(msgData, f, tmp, true));
      buffer.get(tmp);
      buffer.reset();
      for (Field child : f.getChildren()) {
        FieldProcessors.getProcessorClass(child.getFieldType()).read(child, msgData, buffer);
      }
    } else {
      // no children, directly read
      byte[] tmp = new byte[field.getLength()];
      buffer.get(tmp);
      if (log.isDebugEnabled()) {
        log.debug(String.format("field: %s, field-data:%s", f.getFieldName(), Hex.toString(tmp)));
      }
      msgData.setFieldData(f, new FieldData(msgData, f, tmp, true));
    }
  }

  @Override
  public void assemble(Field field, MessageData msgData, ByteArrayOutputStream bos) {
    try {
      FieldData data = msgData.getFieldData(field);
      if (log.isDebugEnabled()) {
        log.debug(
            String.format(
                "writing field:%s, data:%s", field.getFieldName(), Hex.toString(data.getData())));
      }
      bos.write(data.getData());
    } catch (Exception e) {
      new KeedohMessageAssemblyException(e);
    }
  }

  @Override
  public String toStringData(Field field, byte[] data) {
    if (data != null && data.length > 0) {
      String stringData = CharsetUtils.toString(field.getEncodingType(), data);
      return (stringData);
    } else {
      return ("");
    }
  }

  @Override
  public byte[] toBinaryData(Field field, String stringData) {
    if (stringData != null && stringData.length() > 0) {
      return (CharsetUtils.fromString(field.getEncodingType(), stringData));
    } else {
      return (new byte[0]);
    }
  }
}
