package com.daalitoy.apps.keedoh.data.model.impl;

import io.netty.buffer.ByteBuf;

public interface MsgLenIndicator {

  byte[] toNetwork(byte[] data);

  byte[] fromNetwork(ByteBuf buffer);

  boolean packetExists(ByteBuf buffer);
}
