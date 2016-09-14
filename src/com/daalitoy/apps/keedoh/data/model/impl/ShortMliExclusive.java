package com.daalitoy.apps.keedoh.data.model.impl;

import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;

public class ShortMliExclusive implements MsgLenIndicator {

	@Override
	public byte[] toNetwork(byte[] data) {

		ByteBuffer buffer = ByteBuffer.allocate(data.length + 2);
		buffer.putShort((short) data.length);
		buffer.put(data);
		buffer.position(0);
		return (buffer.array());
	}

	@Override
	public byte[] fromNetwork(ChannelBuffer buffer) {
		buffer.markReaderIndex();
		short len = buffer.readShort();
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
	public boolean packetExists(ChannelBuffer buffer) {
		buffer.markReaderIndex();
		short len = buffer.readShort();
		boolean retVal = false;
		if (buffer.readableBytes() >= len) {
			retVal = true;
		} else {
			retVal = false;
		}
		buffer.resetReaderIndex();
		return (retVal);
	}

}
