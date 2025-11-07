package com.daalitoy.apps.keedoh.data.fields.processor;

import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.data.transit.MessageData;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class TerminatedFieldProcessor implements FieldProcessor {

  @Override
  public void read(Field field, MessageData msgData, ByteBuffer buffer) {}

  @Override
  public void assemble(Field field, MessageData msgData, ByteArrayOutputStream outputBuffer) {
    // TODO Auto-generated method stub

  }

  @Override
  public String toStringData(Field field, byte[] data) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public byte[] toBinaryData(Field field, String stringData) {
    // TODO Auto-generated method stub
    return null;
  }
}
