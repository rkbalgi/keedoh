package com.daalitoy.apps.keedoh.data.fields.processor;

import com.daalitoy.apps.keedoh.data.common.ENCODING_TYPE;
import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.data.model.VariableField;
import com.daalitoy.apps.keedoh.data.transit.FieldData;
import com.daalitoy.apps.keedoh.data.transit.MessageData;
import com.daalitoy.apps.keedoh.data.util.CharsetUtils;
import com.daalitoy.apps.keedoh.ui.util.Hex;
import com.google.common.base.Strings;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class VariableFieldProcessor implements FieldProcessor {

  @Override
  public void read(Field field, MessageData msgData, ByteBuffer buffer) {

    VariableField f = (VariableField) field;
    // TODO:: if has children
    int len = readFieldLength(f, buffer);
    if (log.isDebugEnabled()) {
      log.debug(String.format("field: %s, length-indicator:%d", f.getFieldName(), len));
    }

    byte[] tmp = new byte[len];
    buffer.get(tmp);
    if (log.isDebugEnabled()) {
      log.debug(String.format("field: %s, field-data:%s", f.getFieldName(), Hex.toString(tmp)));
    }

    msgData.setFieldData(f, new FieldData(msgData, f, tmp, true));
  }

  private int readFieldLength(VariableField field, ByteBuffer buffer) {
    byte[] tmp = new byte[field.getIndicatorLength()];
    buffer.get(tmp);
    int len = CharsetUtils.toInt(field.getIndicatorEncodingType(), tmp);
    return (len);
  }

  @Override
  public void assemble(Field field, MessageData msgData, ByteArrayOutputStream bos) {
    VariableField f = (VariableField) field;
    try {
      byte[] vli = getFieldLen(f, msgData.getFieldData(f).getData().length);

      if (log.isDebugEnabled()) {
        log.debug(
            String.format("writing field:%s, vli:%s", field.getFieldName(), Hex.toString(vli)));
        log.debug(
            String.format(
                "writing field:%s, data:%s",
                field.getFieldName(), Hex.toString(msgData.getFieldData(field).getData())));
      }

      bos.write(vli);
      bos.write(msgData.getFieldData(field).getData());
    } catch (Exception e) {
      new KeedohMessageAssemblyException(e);
    }
  }

  private byte[] getFieldLen(VariableField f, int length) {
    byte[] vli =
        VariableFieldUtils.getLenIndicator(
            f.getIndicatorEncodingType(), f.getIndicatorLength(), length);
    return (vli);
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

class VariableFieldUtils {

  public static byte[] getLenIndicator(ENCODING_TYPE encodingType, int iLength, int length) {
    String tmp = null;
    switch (encodingType) {
      case BCD:
        {
          tmp = String.format("%s", Integer.toString(length));
          tmp = Strings.padStart(tmp, iLength * 2, '0');
          return (Hex.fromString(tmp));
        }
      case BINARY:
        {
          tmp = String.format("%s", Integer.toHexString(length));
          tmp = Strings.padStart(tmp, iLength * 2, '0');
          return (Hex.fromString(tmp));
        }
      case ASCII:
      case EBCDIC:
        {
          tmp = String.format("%s", Integer.toString(length));
          tmp = Strings.padStart(tmp, iLength, '0');
          byte[] data = CharsetUtils.fromString(encodingType, tmp);
          return (data);
        }
    }
    throw new IllegalArgumentException("unsupported encoding type:" + encodingType);
  }
}
