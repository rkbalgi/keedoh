package com.daalitoy.apps.keedoh.data.fields.processor;

import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.data.transit.MessageData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public interface FieldProcessor {

    Logger log = LogManager.getLogger(FieldProcessor.class);

    void read(Field field, MessageData msgData, ByteBuffer buffer);

    void assemble(Field field, MessageData msgData, ByteArrayOutputStream outputBuffer);

    String toStringData(Field field, byte[] data);

    byte[] toBinaryData(Field field, String stringData);
}
