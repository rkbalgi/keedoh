package com.daalitoy.apps.keedoh.ui.table.data;

import com.daalitoy.apps.keedoh.data.transit.MessageData;
import com.daalitoy.apps.keedoh.messaging.MessageStore;
import javax.swing.table.AbstractTableModel;

public class MessageStoreTableModel extends AbstractTableModel {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private boolean isClientMsgStore = false;

  public MessageStoreTableModel(boolean isClientMsgStore) {
    this.isClientMsgStore = isClientMsgStore;
  }

  @Override
  public int getColumnCount() {
    return (3);
  }

  public boolean isCellEditable(int row, int col) {
    return (false);
  }

  public String getColumnName(int col) {
    if (col == 0) {
      return ("Specification Name");
    } else if (col == 1) {
      return ("Send Time");
    } else if (col == 2) {
      return ("Receive Time");
    } else {
      return (null);
    }
  }

  @Override
  public int getRowCount() {
    if (isClientMsgStore) {
      return (MessageStore.getClientMsgStoreSize());
    } else {
      return (MessageStore.getServerMsgStoreSize());
    }
  }

  @Override
  public Object getValueAt(int arg0, int arg1) {
    MessageData reqMsgData = null, respMsgData = null;
    if (isClientMsgStore) {
      reqMsgData = MessageStore.getClientMsg(arg0);
      respMsgData = MessageStore.getClientResponseLeg(reqMsgData);
    } else {
      reqMsgData = MessageStore.getServerMsg(arg0);
      respMsgData = MessageStore.getServerResponseLeg(reqMsgData);
    }
    if (arg1 == 0) {
      return (reqMsgData.getMessage().getSpec().getSpecName());
    } else if (arg1 == 1) {
      return (reqMsgData.getProcessTime());
    } else if (arg1 == 2) {

      if (respMsgData != null) {
        return (respMsgData.getProcessTime());
      } else {
        return ("NA");
      }
    }
    return null;
  }
}
