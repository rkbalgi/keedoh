package com.daalitoy.apps.keedoh.net.server;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.WriteCompletionEvent;

import com.daalitoy.apps.keedoh.data.model.ListenerConfig;
import com.daalitoy.apps.keedoh.data.model.Spec;
import com.daalitoy.apps.keedoh.messaging.MessagingManager;
import com.daalitoy.apps.keedoh.net.client.Connectors;

public class ListenerChannelHandler extends SimpleChannelHandler {

	private Logger log = Logger.getLogger(getClass());

	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		log.error(e.getCause());
	}

	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {

		ListenerConfig config = Listeners.getConfigForChannel(e.getChannel());
		log.debug(config);
		ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
		if (config.getMli().getMliImpl().packetExists(buffer)) {
			byte[] data = config.getMli().getMliImpl().fromNetwork(buffer);
			Spec spec = Listeners.getSpecForConfig(config);
			MessagingManager.getInstance().handleIncomingServerMessage(spec,
					data,config.getProcessorScript());

		}

	}

	public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e) {

	}

	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
		log.debug(String.format("channel closed - %s", e.getChannel()
				.getRemoteAddress()));
		Listeners.close(e.getChannel());

	}

	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		log.debug(String.format("channel opened - %s", e.getChannel()
				.getRemoteAddress()));
		Listeners.add(e.getChannel());
	}
}
