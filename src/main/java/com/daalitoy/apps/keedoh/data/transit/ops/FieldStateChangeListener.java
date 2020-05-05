package com.daalitoy.apps.keedoh.data.transit.ops;

import com.daalitoy.apps.keedoh.data.model.BitmappedField;
import com.daalitoy.apps.keedoh.data.transit.FieldData;
import com.daalitoy.apps.keedoh.data.transit.MessageData;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public abstract class FieldStateChangeListener {

    protected static Predicate<Object> BITMAP_CLASS_CHECK = Predicates
            .instanceOf(BitmappedField.class);

    public static FieldStateChangeListener fixedFieldStateListener() {
        return (FixedFieldStateChangeListener.getInstance());
    }

    public static FieldStateChangeListener terminatedFieldStateListener() {
        return (TerminatedFieldStateChangeListener.getInstance());
    }

    public static FieldStateChangeListener variableFieldStateListener() {
        return (VariableFieldStateChangeListener.getInstance());
    }

    public static FieldStateChangeListener bitmappedFieldStateListener() {
        return (BitmappedFieldStateChangeListener.getInstance());
    }

    public abstract void fieldSelectionChanged(MessageData msgData,
                                               FieldData fieldData, boolean selected);

    public abstract void fieldDataChanged(FieldData fieldData,
                                          boolean stringFlavour);
}
