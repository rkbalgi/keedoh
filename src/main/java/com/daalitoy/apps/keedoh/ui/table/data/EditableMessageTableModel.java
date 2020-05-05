package com.daalitoy.apps.keedoh.ui.table.data;

import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.data.model.Message;
import com.daalitoy.apps.keedoh.data.transit.MessageData;

import javax.swing.table.AbstractTableModel;

public class EditableMessageTableModel extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Message msg;
    private MessageData msgData;

    public EditableMessageTableModel(Message msg, boolean request) {
        super();
        this.msg = msg;
        msgData = new MessageData(msg, request);
    }

    public EditableMessageTableModel(MessageData msgData) {
        super();
        this.msgData = msgData;
    }

    public int getColumnCount() {
        return (3);
    }

    public String getColumnName(int col) {
        // Preconditions.checkArgument(!(col > 0 && col < 3));

        if (col == 0) {
            return ("Selected");
        } else if (col == 1) {
            return ("Field Name");
        } else if (col == 2) {
            return ("Field Value");
        }

        return ("");
    }

    public int getRowCount() {
        return (msgData.getFieldCount());
    }

    public boolean isCellEditable(int row, int col) {

        return (col == 2) || (col == 0);

    }

    public void setValueAt(Object obj, int row, int col) {

        if (col == 0) {
            msgData.getFieldData(row).setSelected((Boolean) obj);
        } else if (col == 2) {
            msgData.getFieldData(row).setStringData((String) obj);
        }
        fireTableDataChanged();
    }

    public Class<?> getColumnClass(int col) {

        if (col == 0) {
            return (Boolean.class);
        } else {
            return (String.class);
        }
    }

    public Object getValueAt(int row, int col) {

        // Preconditions.checkArgument(!(col > 0 && col < 3));
        if (col == 0) {
            return (msgData.getFieldData(row).isSelected());
        } else if (col == 1) {
            Field f = msgData.getFieldData(row).getField();
            if (f.getSequence() != -1) {
                return (String.format("(%d) %s", f.getSequence(),
                        f.getFieldName()));
            } else {
                return (f.getFieldName());
            }

        } else if (col == 2) {
            return (msgData.getFieldData(row).getStringData());
        }
        return (null);

    }

    public MessageData getMsgData() {
        return (msgData);
    }
}
