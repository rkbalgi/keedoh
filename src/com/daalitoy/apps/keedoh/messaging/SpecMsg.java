package com.daalitoy.apps.keedoh.messaging;

import com.daalitoy.apps.keedoh.data.model.BitmappedField;
import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.data.model.MessageFragment;
import com.daalitoy.apps.keedoh.data.transit.MessageData;

public class SpecMsg {

	private MessageData reqMsgData;
	private MessageData respMsgData;

	public SpecMsg(MessageData reqMsgData, MessageData respMsgData) {
		this.reqMsgData = reqMsgData;
		this.respMsgData = respMsgData;
	}

	public String getValue(int fragmentType, String fieldName) {
		MessageData msgData = getMsgData(fragmentType);
		MessageFragment fragment = getFragment(fragmentType);

		Field f = fragment.getFieldByName(fieldName);
		return (msgData.getFieldData(f).getStringData());

	}

	public void setValue(int fragmentType, String fieldName, String value) {
		MessageData msgData = getMsgData(fragmentType);
		MessageFragment fragment = getFragment(fragmentType);

		Field f = fragment.getFieldByName(fieldName);
		msgData.getFieldData(f).setStringData(value);
		msgData.getFieldData(f).setSelected(true);

	}

	public boolean isSet(int fragmentType, String fieldName, int sequence) {
		MessageData msgData = getMsgData(fragmentType);
		MessageFragment fragment = getFragment(fragmentType);

		BitmappedField bmp = (BitmappedField) fragment
				.getFieldByName(fieldName);
		return (msgData.getFieldData(bmp.getChild(sequence)).isSelected());

	}

	public void set(int fragmentType, String fieldName, int sequence,
			boolean on) {
		MessageData msgData = getMsgData(fragmentType);
		MessageFragment fragment = getFragment(fragmentType);

		BitmappedField bmp = (BitmappedField) fragment
				.getFieldByName(fieldName);
		msgData.getFieldData(bmp.getChild(sequence)).setSelected(on);

	}

	public String getValue(int fragmentType, String fieldName, int sequence) {
		MessageData msgData = getMsgData(fragmentType);
		MessageFragment fragment = getFragment(fragmentType);

		BitmappedField bmp = (BitmappedField) fragment
				.getFieldByName(fieldName);
		return (msgData.getFieldData(bmp.getChild(sequence)).getStringData());

	}

	public void setValue(int fragmentType, String fieldName, int sequence,
			String value) {
		MessageData msgData = getMsgData(fragmentType);
		MessageFragment fragment = getFragment(fragmentType);

		BitmappedField bmp = (BitmappedField) fragment
				.getFieldByName(fieldName);
		msgData.getFieldData(bmp.getChild(sequence)).setStringData(value);
		msgData.getFieldData(bmp.getChild(sequence)).setSelected(true);

	}

	private MessageFragment getFragment(int fragmentType) {
		MessageFragment fragment = null;
		if (fragmentType == MessageData.REQUEST) {
			fragment = reqMsgData.getMessage().getRequestFragment();
		} else {
			fragment = respMsgData.getMessage().getRequestFragment();
		}
		return (fragment);
	}

	private MessageData getMsgData(int fragmentType) {
		MessageData msgData = null;
		if (fragmentType == MessageData.REQUEST) {
			msgData = reqMsgData;
		} else {
			msgData = respMsgData;
		}
		return (msgData);

	}
}
