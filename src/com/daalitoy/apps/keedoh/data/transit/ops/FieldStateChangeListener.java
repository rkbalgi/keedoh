package com.daalitoy.apps.keedoh.data.transit.ops;

import com.daalitoy.apps.keedoh.data.model.BitmappedField;
import com.daalitoy.apps.keedoh.data.transit.FieldData;
import com.daalitoy.apps.keedoh.data.transit.MessageData;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public abstract class FieldStateChangeListener {

	protected static Predicate<Object> BITMAP_CLASS_CHECK = Predicates
			.instanceOf(BitmappedField.class);

	public abstract void fieldSelectionChanged(MessageData msgData,
			FieldData fieldData, boolean selected);

	public abstract void fieldDataChanged(FieldData fieldData,
			boolean stringFlavour);

	public static FieldStateChangeListener fixedFieldStateListener() {
		return (FixedFieldStateChangeListener.getInstance());
	}

	public static FieldStateChangeListener fixedTerminatedFieldStateListener() {
		return (TerminatedFieldStateChangeListener.getInstance());
	}

	public static FieldStateChangeListener fixedVariableFieldStateListener() {
		return (VariableFieldStateChangeListener.getInstance());
	}

	public static FieldStateChangeListener fixedBitmappedFieldStateListener() {
		return (BitmappedFieldStateChangeListener.getInstance());
	}
}
