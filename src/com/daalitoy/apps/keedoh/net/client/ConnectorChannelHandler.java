package com.daalitoy.apps.keedoh.net.client;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.WriteCompletionEvent;

import com.daalitoy.apps.keedoh.data.model.ConnectorConfig;
import com.daalitoy.apps.keedoh.data.model.Spec;
import com.daalitoy.apps.keedoh.messaging.MessagingManager;
import com.daalitoy.apps.keedoh.ui.util.Hex;

public class ConnectorChannelHandler extends SimpleChannelHandler {

	private Logger log = Logger.getLogger(getClass());

	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		log.error(e);
	}

	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {

		ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
		log.debug("msg received with readable bytes -" + buffer.readableBytes());
		ConnectorConfig config = Connectors.getConfigForChannel(e.getChannel());
		Spec spec = Connectors.getSpecForConfig(config);

		if (config.getMli().getMliImpl().packetExists(buffer)) {
			byte[] data = config.getMli().getMliImpl().fromNetwork(buffer);

			// this is a connector setup, so lets check if this a response
			// to an request

			MessagingManager.getInstance().handleIncomingClientMessage(spec,
					data);
		}

	}

	public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e) {
		log.debug("write completed -" + e.getWrittenAmount() + " bytes.");
	}

	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
		log.debug(String.format("channel closed - %s", e.getChannel()
				.getRemoteAddress()));

		Connectors.close(e.getChannel());

	}

	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		log.debug(String.format("channel opened - %s", e.getChannel()
				.getRemoteAddress()));
		Connectors.add(e.getChannel());
	}
}
