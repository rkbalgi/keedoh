package com.daalitoy.apps.keedoh.messaging;

import com.daalitoy.apps.keedoh.data.transit.MessageData;

public class MessageTimeoutTask implements Runnable {

  private MessageData msgData;

  public MessageTimeoutTask(MessageData msgData) {
    this.msgData = msgData;
  }

  @Override
  public void run() {
    MessagingManager.getInstance().removeFlightEntry(msgData);
    msgData.getLock().lock();
    try {
      msgData.getCondition().signalAll();
    } finally {
      msgData.getLock().unlock();
    }
  }
}
