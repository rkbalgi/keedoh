package com.daalitoy.apps.keedoh.data.model.impl;

import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;

public interface MsgLenIndicator {

	public byte[] toNetwork(byte[] data);
	public byte[] fromNetwork(ChannelBuffer buffer);
	public boolean packetExists(ChannelBuffer buffer);

}
