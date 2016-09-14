package com.daalitoy.apps.keedoh.net.client;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.daalitoy.apps.keedoh.data.model.ConnectorConfig;
import com.daalitoy.apps.keedoh.data.model.ListenerConfig;
import com.daalitoy.apps.keedoh.data.model.MLI;
import com.daalitoy.apps.keedoh.data.model.Spec;
import com.daalitoy.apps.keedoh.data.transit.MessageData;
import com.daalitoy.apps.keedoh.messaging.MessagingManager;
import com.daalitoy.apps.keedoh.net.KeedohNetException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Connectors {

	private static Logger log = Logger.getLogger(Connectors.class);
	private static Map<ConnectorConfig, Channel> connectorsMap = Maps
			.newHashMap();
	private static List<ConnectorConfig> configs = Lists.newArrayList();
	private static Map<ConnectorConfig, Spec> specConfigMap = Maps.newHashMap();

	public static void register(ConnectorConfig config, Spec spec) {
		specConfigMap.put(config, spec);
	}

	public static void send(MessageData msgData) {

		ConnectorConfig config = msgData.getMessage().getConnectorConfig();
		byte[] data = msgData.assemble(true);
		MLI mli = config.getMli();
		byte[] packet = mli.getMliImpl().toNetwork(data);

		// lets add to the list of in-flight messages
		MessagingManager.getInstance().makeFlightEntry(msgData);

		connect(msgData.getMessage().getConnectorConfig());
		connectorsMap.get(config).write(ChannelBuffers.copiedBuffer(packet));

	}

	public static void connect(ConnectorConfig config) {

		if (connectorsMap.containsKey(config)
				&& connectorsMap.get(config).isConnected()) {
			return;
		} else {
			log.info("attempting to connect..");

			NioClientSocketChannelFactory factory = new NioClientSocketChannelFactory();
			ClientBootstrap bootstrap = new ClientBootstrap(factory);
			bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

				@Override
				public ChannelPipeline getPipeline() throws Exception {
					return (Channels.pipeline(new ConnectorChannelHandler()));
				}

			});
			ChannelFuture future = bootstrap.connect(new InetSocketAddress(
					config.getIp(), config.getPort()));
			try {
				future.await(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				log.error(e);
			}
			if (future.isDone()) {
				configs.add(config);
			} else {
				throw new KeedohNetException("connect failed.");
			}

		}

	}

	public static void disconnect(ConnectorConfig connectorConfig) {
		try {
			connectorsMap.get(connectorConfig).close()
					.await(10, TimeUnit.SECONDS);
			connectorsMap.remove(connectorConfig);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void close(Channel channel) {
		connectorsMap.remove(getConfigForChannel(channel));
	}

	public static void add(Channel channel) {
		connectorsMap.put(getConfigForChannel(channel), channel);
	}

	public static void add(ConnectorConfig config, Channel channel) {
		connectorsMap.put(config, channel);
	}

	static ConnectorConfig getConfigForChannel(Channel channel) {
		int port = ((InetSocketAddress) channel.getRemoteAddress()).getPort();
		System.out.println(port);
		for (ConnectorConfig config : configs) {
			System.out.println(config.getPort());
			if (port == config.getPort()) {
				return (config);
			}
		}
		return null;
	}

	public static boolean isConnected(ConnectorConfig config) {
		return (connectorsMap.containsKey(config));
	}

	public static Spec getSpecForConfig(ConnectorConfig config) {
		return (specConfigMap.get(config));

	}

}
