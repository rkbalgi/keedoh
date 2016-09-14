package com.daalitoy.apps.keedoh.data.transit;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.apache.log4j.Logger;

import com.daalitoy.apps.keedoh.data.common.FIELD_TYPE;
import com.daalitoy.apps.keedoh.data.fields.processor.BitmappedFieldProcessor;
import com.daalitoy.apps.keedoh.data.fields.processor.FieldProcessor;
import com.daalitoy.apps.keedoh.data.fields.processor.FixedFieldProcessor;
import com.daalitoy.apps.keedoh.data.fields.processor.TerminatedFieldProcessor;
import com.daalitoy.apps.keedoh.data.fields.processor.VariableFieldProcessor;
import com.daalitoy.apps.keedoh.data.model.Bit;
import com.daalitoy.apps.keedoh.data.model.BitmappedField;
import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.data.model.Model;
import com.daalitoy.apps.keedoh.data.transit.ops.FieldStateChangeListener;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class FieldData {

	protected static final Logger log = Logger.getLogger(FieldData.class);

	private Field field;
	private byte[] data;
	private String stringData = "";
	private boolean selected;
	private FieldStateChangeListener listener;

	private static final Map<FIELD_TYPE, FieldProcessor> processorMap = Maps
			.newHashMap();
	static {
		processorMap.put(FIELD_TYPE.FIXED, new FixedFieldProcessor());
		processorMap.put(FIELD_TYPE.TERMINATED, new TerminatedFieldProcessor());
		processorMap.put(FIELD_TYPE.VARIABLE, new VariableFieldProcessor());
		processorMap.put(FIELD_TYPE.BITMAPPED, new BitmappedFieldProcessor());
	}

	// for bitmapped field's

	private Bit bitmap = new Bit(new byte[16]);
	private MessageData msgData;

	public FieldData(MessageData msgData, Field field, byte[] data,
			boolean selected) {
		this.msgData = msgData;
		this.field = field;
		this.data = data;
		this.selected = selected;

		if (field.getFieldType() == FIELD_TYPE.BITMAPPED) {
			if (data != null)
				bitmap = new Bit(data);
		}

	}

	public Field getField() {
		return (field);
	}

	public byte[] getData() {

		if (field instanceof BitmappedField) {
			return (bitmap.getData());
		}
		return data;
	}

	public void setOn(int position) {
		Preconditions.checkArgument(field instanceof BitmappedField);
		bitmap.setOn(position);
	}

	public void setOff(int position) {
		Preconditions.checkArgument(field instanceof BitmappedField);
		bitmap.setOff(position);
	}

	public void assemble(ByteArrayOutputStream bos) {
		processorMap.get(field.getFieldType()).assemble(field, msgData, bos);
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getStringData() {
		String stringData = processorMap.get(field.getFieldType())
				.toStringData(field, getData());
		return (stringData);
	}

	public void setStringData(String stringData) {
		this.stringData = stringData;
		this.data = processorMap.get(field.getFieldType()).toBinaryData(field,
				stringData);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		listener.fieldSelectionChanged(msgData, this, selected);
	}

	// listeners for field change events
	public FieldStateChangeListener getListener() {
		return listener;
	}

	public void setListener(FieldStateChangeListener listener) {
		this.listener = listener;
	}

	public boolean isOn(int position) {
		return (bitmap.isOn(position));
	}

}
