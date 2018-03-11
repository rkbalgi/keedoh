package com.daalitoy.apps.keedoh.data.fields.processor;

import com.daalitoy.apps.keedoh.data.common.ENCODING_TYPE;
import com.daalitoy.apps.keedoh.data.model.BitmappedField;
import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.data.transit.FieldData;
import com.daalitoy.apps.keedoh.data.transit.MessageData;
import com.daalitoy.apps.keedoh.data.util.Bits;
import com.daalitoy.apps.keedoh.data.util.CharsetUtils;
import com.daalitoy.apps.keedoh.ui.util.Hex;
import com.google.common.base.Preconditions;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class BitmappedFieldProcessor implements FieldProcessor {

    private static final byte[] EMPTY_BYTES = new byte[8];

    @Override
    public void read(Field field, MessageData msgData, ByteBuffer buffer) {

        BitmappedField f = (BitmappedField) field;

        // read 8 bytes and see if high bit is on and if so
        // read further
        Preconditions.checkArgument(
                f.getEncodingType() == ENCODING_TYPE.BINARY,
                "ASCII/EBCDIC encodings for bitmap not supported yet.");
        byte[] tmp = new byte[16];
        buffer.get(tmp, 0, 8);
        if (Bits.highBitOn(tmp[0])) {
            // secondary bit present
            buffer.get(tmp, 8, 16);
        }

        if (Arrays.equals(Arrays.copyOfRange(tmp, 8, 16), EMPTY_BYTES)) {
            tmp = Arrays.copyOfRange(tmp, 0, 8);
        }

        if (log.isDebugEnabled()) {
            log.debug(String.format("field: %s, field-data:%s",
                    f.getFieldName(), Hex.toString(tmp)));
        }

        FieldData fData = new FieldData(msgData, f, tmp, true);
        msgData.setFieldData(f, fData);

        for (Field child : f.getChildren()) {
            if (fData.isOn(child.getSequence())) {
                FieldProcessors.getProcessorClass(child.getFieldType()).read(
                        child, msgData, buffer);
            }
        }

    }

    @Override
    public void assemble(Field field, MessageData msgData,
                         ByteArrayOutputStream bos) {
        try {
            Preconditions.checkArgument(
                    field.getEncodingType() == ENCODING_TYPE.BINARY,
                    "ASCII/EBCDIC encodings for bitmap not supported yet.");
            bos.write(msgData.getFieldData(field).getData());
            if (log.isDebugEnabled()) {
                log.debug(String.format("writing field:%s, data:%s",
                        field.getFieldName(),
                        Hex.toString(msgData.getFieldData(field).getData())));
            }
            FieldData fData = msgData.getFieldData(field);
            for (Field child : field.getChildren()) {

                if (fData.isOn(child.getSequence())) {
                    FieldProcessors.getProcessorClass(child.getFieldType())
                            .assemble(child, msgData, bos);
                }
            }
        } catch (Exception e) {
            new KeedohMessageAssemblyException(e);
        }

    }

    @Override
    public String toStringData(Field field, byte[] data) {
        return (Hex.toString(data));
    }

    @Override
    public byte[] toBinaryData(Field field, String stringData) {
        return (CharsetUtils.fromString(field.getEncodingType(), stringData));
    }

}
