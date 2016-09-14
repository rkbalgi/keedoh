package com.daalitoy.apps.keedoh.data.fields.processor;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.data.transit.FieldData;
import com.daalitoy.apps.keedoh.data.transit.MessageData;

public interface FieldProcessor {

	public static final Logger log = Logger.getLogger(FieldData.class);
	public void read(Field field, MessageData msgData, ByteBuffer buffer);

	public void assemble(Field field, MessageData msgData,
			ByteArrayOutputStream outputBuffer);

	public String toStringData(Field field, byte[] data);

	public byte[] toBinaryData(Field field, String stringData);
}
