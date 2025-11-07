package com.daalitoy.apps.keedoh.ui.table.data;

import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.data.model.MessageSegment;
import com.daalitoy.apps.keedoh.data.transit.FieldData;
import com.daalitoy.apps.keedoh.data.transit.MessageData;
import com.google.common.collect.Lists;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class MessageDataTableModel extends AbstractTableModel {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  List<FieldData> fieldDataList = Lists.newArrayList();
  private MessageData msgData;
  private MessageSegment fragment;

  public MessageDataTableModel(MessageData msgData, MessageSegment fragment) {
    this.msgData = msgData;
    this.fragment = fragment;
    for (Field field : fragment.getFields()) {
      init(field);
    }
  }

  private void init(Field field) {
    fieldDataList.add(msgData.getFieldData(field));
    for (Field child : field.getChildren()) {
      init(child);
    }
  }

  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (FieldData data : fieldDataList) {
      builder.append(data.getField().getFieldName() + ",");
    }
    builder.append("\n");
    for (FieldData data : fieldDataList) {
      builder.append(data.getStringData() + ",");
    }
    builder.append("\n");

    return (builder.toString());
  }

  @Override
  public int getColumnCount() {
    return (fieldDataList.size());
  }

  @Override
  public int getRowCount() {
    return (1);
  }

  public String getColumnName(int col) {
    return (fieldDataList.get(col).getField().getFieldName());
  }

  @Override
  public Object getValueAt(int arg0, int arg1) {
    return (fieldDataList.get(arg1).getStringData());
  }
}
