package com.daalitoy.apps.keedoh.data.model.impl;

import io.netty.buffer.ByteBuf;
import java.nio.ByteBuffer;

public class ShortMliInclusive implements MsgLenIndicator {
  @Override
  public byte[] toNetwork(byte[] data) {

    ByteBuffer buffer = ByteBuffer.allocate(data.length + 2);
    buffer.putShort((short) (data.length + 2));
    buffer.put(data);
    buffer.position(0);
    return (buffer.array());
  }

  @Override
  public byte[] fromNetwork(ByteBuf buffer) {
    buffer.markReaderIndex();
    short len = (short) (buffer.readShort() - 2);
    if (buffer.readableBytes() >= len) {
      byte[] tmp = new byte[len];
      buffer.readBytes(tmp);
      return (tmp);
    } else {
      buffer.resetReaderIndex();
    }
    return (null);
  }

  @Override
  public boolean packetExists(ByteBuf buffer) {
    buffer.markReaderIndex();
    short len = (short) (buffer.readShort() - 2);
    boolean retVal = false;
    retVal = buffer.readableBytes() >= len;
    buffer.resetReaderIndex();
    return (retVal);
  }
}
