package com.daalitoy.apps.keedoh.data.transit;

import com.daalitoy.apps.keedoh.data.fields.processor.FieldProcessors;
import com.daalitoy.apps.keedoh.data.model.*;
import com.daalitoy.apps.keedoh.data.transit.ops.FieldStateChangeListener;
import com.daalitoy.apps.keedoh.ui.util.Hex;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MessageData implements Cloneable {

    public static final int REQUEST = 0;
    public static final int RESPONSE = 1;

    protected final Logger log = LogManager.getLogger(getClass());
    private Message msg;
    private List<Field> fields = Lists.newArrayList();
    private Map<Field, FieldData> fieldValueMap = Maps.newLinkedHashMap();
    private boolean request = true;

    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private MessageData cloneRef;
    private Date processDate;

    public MessageData(Message msg, boolean request) {
        this.msg = msg;
        this.request = request;
        initMap();
        fields.addAll(fieldValueMap.keySet());
        if (log.isDebugEnabled()) {
            log.debug("fieldMap: " + fieldValueMap);
            log.debug("fields: " + fields);
        }
    }

    /**
     * Build MsgData i.e. parse the trace and create a structure
     *
     * @param msg
     * @param trace
     * @param request
     * @return
     */
    public static MessageData build(Message msg, String trace, boolean request) {

        String normalizedTrace = trace.replaceAll("\\W+", "");
        System.out.println(normalizedTrace);
        ByteBuffer buffer = ByteBuffer.wrap(Hex.fromString(normalizedTrace));
        MessageData msgData = new MessageData(msg, true);
        MessageSegment fragment = null;
        if (request) fragment = msg.getRequestSegment();
        else fragment = msg.getResponseSegment();

        List<Field> fields = fragment.getFields();
        for (Field field : fields) {
            FieldProcessors.getProcessorClass(field.getFieldType()).read(field, msgData, buffer);
        }

        return msgData;
    }

    public static Message determineMessageType(Spec spec, byte[] data) {

        ByteBuffer buffer = ByteBuffer.wrap(data);
        Message msg = spec.getMessages().get(0);
        MessageData msgData = new MessageData(msg, true);

        MessageSegment fragment = msg.getRequestSegment();
        List<Field> fields = fragment.getFields();

        for (Field field : fields) {
            FieldProcessors.getProcessorClass(field.getFieldType()).read(field, msgData, buffer);
            if (field.isMti()) {
                // we're done
                String mtiValue = msgData.getFieldData(field).getStringData();
                // get the message which has this MTI
                for (Message lMsg : spec.getMessages()) {

                    if (lMsg.getRequestSegment().getMti().contains(mtiValue)) {
                        return lMsg;
                    }

          /*for (String requestMti : lMsg.getRequestSegment().getMti()) {
            if (requestMti.equals(mtiValue)) {
              return (lMsg);
            }
          }
          for (String requestMti : lMsg.getResponseMti()) {
            if (requestMti.equals(mtiValue)) {
              return (lMsg);
            }
          }*/
                }
            }
        }

        return (null);
    }

    public Object clone() {
        MessageData clone = new MessageData(msg, this.request);
        for (Field field : fieldValueMap.keySet()) {
            FieldData fData = fieldValueMap.get(field);
            clone.fieldValueMap.get(field).setStringData(fData.getStringData());
            clone.fieldValueMap.get(field).setSelected(fData.isSelected());
        }
        clone.processDate = this.processDate;
        return (clone);
    }

    private void initMap() {
        List<Field> fields = null;
        if (request) {
            System.out.println("req fields");
            fields = msg.getRequestSegment().getFields();
        } else {
            System.out.println("resp fields");
            fields = msg.getResponseSegment().getFields();
        }
        for (Field f : fields) {
            processField(f);
        }
    }

    public boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public Condition getCondition() {
        return condition;
    }

    public Message getMessage() {
        return (msg);
    }

    private void processField(Field field) {
        FieldData fData = new FieldData(this, field, null, false);
        switch (field.getFieldType()) {
            case FIXED:
                fData.setListener(FieldStateChangeListener.fixedFieldStateListener());
                break;

            case TERMINATED:
                fData.setListener(FieldStateChangeListener.terminatedFieldStateListener());
                break;

            case VARIABLE:
                fData.setListener(FieldStateChangeListener.variableFieldStateListener());
                break;

            case BITMAPPED:
                fData.setListener(FieldStateChangeListener.bitmappedFieldStateListener());
                break;
        }
        fieldValueMap.put(field, fData);
        for (Field f : field.getChildren()) {
            processField(f);
        }
    }

    public FieldData getFieldData(int index) {
        return (fieldValueMap.get(fields.get(index)));
    }

    public void setFieldData(Field field, FieldData fieldData) {
        fieldValueMap.get(field).setData(fieldData.getData());
        fieldValueMap.get(field).setSelected(fieldData.isSelected());
    }

    public int getFieldCount() {
        return (fields.size());
    }

    public FieldData getFieldData(Field field) {
        return (fieldValueMap.get(field));
    }

    /**
     * Build trace from MsgData
     *
     * @param b
     * @return
     */
    public byte[] assemble(boolean request) {

        MessageSegment fragment = null;
        if (request) {
            fragment = msg.getRequestSegment();
        } else {
            fragment = msg.getResponseSegment();
        }

        List<Field> fields = fragment.getFields();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (Field field : fields) {
            FieldProcessors.getProcessorClass(field.getFieldType()).assemble(field, this, bos);
        }
        return (bos.toByteArray());
    }

    public MessageData getCloneRef() {
        return (cloneRef);
    }

    public void setCloneRef(MessageData cloneRef) {
        this.cloneRef = cloneRef;
    }

    public Date getProcessTime() {
        return (processDate);
    }

    public void setProcessTime(Date date) {
        this.processDate = date;
    }

    public List<Field> getResponseFields() {
        return (msg.getResponseSegment().getFields());
    }

    public List<Field> getRequestFields() {
        return (msg.getRequestSegment().getFields());
    }

    public static class FIELD {
        public static Field getField(MessageData msgData, String fieldName) {
            for (Field f : msgData.fields) {
                if (f.getFieldName().equals(fieldName)) {
                    return (f);
                }
            }
            return (null);
        }

        public static String getValue(MessageData msgData, Field field) {
            return (msgData.fieldValueMap.get(field).getStringData());
        }

        public static void setValue(MessageData msgData, Field field, String value) {
            msgData.fieldValueMap.get(field).setStringData(value);
            msgData.fieldValueMap.get(field).setSelected(true);
        }
    }

    public static class BITMAP {
        public static void setOn(MessageData msgData, Field field, int sequence) {
            BitmappedField bmp = (BitmappedField) field;
            Field f = bmp.getChild(sequence);
            msgData.fieldValueMap.get(field).setSelected(true);
        }

        public static void setOff(MessageData msgData, Field field, int sequence) {
            BitmappedField bmp = (BitmappedField) field;
            Field f = bmp.getChild(sequence);
            msgData.fieldValueMap.get(f).setSelected(false);
        }

        public static String getValue(MessageData msgData, Field field, int sequence) {
            BitmappedField bmp = (BitmappedField) field;
            Field f = bmp.getChild(sequence);
            return (msgData.fieldValueMap.get(f).getStringData());
        }

        public static void setValue(MessageData msgData, Field field, int sequence, String value) {
            BitmappedField bmp = (BitmappedField) field;
            Field f = bmp.getChild(sequence);
            msgData.fieldValueMap.get(f).setStringData(value);
            msgData.fieldValueMap.get(f).setSelected(true);
        }
    }
}
