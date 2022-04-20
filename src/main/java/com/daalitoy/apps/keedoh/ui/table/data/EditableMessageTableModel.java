package com.daalitoy.apps.keedoh.ui.table.data;

import com.daalitoy.apps.keedoh.data.model.Field;

import com.daalitoy.apps.keedoh.data.transit.MessageData;
import com.google.common.collect.Lists;
import io.github.rkbalgi.iso4k.IsoField;
import io.github.rkbalgi.iso4k.Message;
import io.github.rkbalgi.iso4k.Transaction;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;

public class EditableMessageTableModel extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Message msg;
    private Transaction transaction;

    public EditableMessageTableModel(Transaction transaction, boolean request) {
        super();
        this.transaction = transaction;
    }

    public EditableMessageTableModel(Message msg) {
        super();
        this.msg = msg;
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

        if (transaction != null) {
            return flatSize(transaction.request());
        } else {
            return (flatSize(msg));
        }
    }

    private int flatSize(Message msg) {

        ArrayList<Object> fieldsByRow = Lists.newArrayList();
        msg.getMessageSegment().fields().forEach(f -> {
            fieldsByRow.add(f);
            if (f.hasChildren()) {
                addChildren(f, fieldsByRow);
            }

        });
        return fieldsByRow.size();
    }

    private void addChildren(IsoField f, ArrayList<Object> fieldsByRow) {
        Arrays.stream(f.getChildren()).iterator().forEachRemaining(e -> {
            fieldsByRow.add(e);
            if (e.hasChildren()) {
                addChildren(e, fieldsByRow);
            }
        });
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
