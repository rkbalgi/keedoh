package com.daalitoy.apps.keedoh.net.server;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.daalitoy.apps.keedoh.data.model.ListenerConfig;
import com.daalitoy.apps.keedoh.data.model.Spec;
import com.daalitoy.apps.keedoh.data.transit.MessageData;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Listeners {

	private static Map<ListenerConfig, Channel> listenersMap = Maps
			.newHashMap();
	private static Map<ListenerConfig, ChannelGroup> channelGroupMap = Maps
			.newHashMap();
	private static Map<ListenerConfig, ServerBootstrap> bootStrapMap = Maps
			.newHashMap();

	private static List<ListenerConfig> configs = Lists.newArrayList();
	private static Map<ListenerConfig, Spec> specConfigMap = Maps.newHashMap();

	public static void register(ListenerConfig config, Spec spec) {
		specConfigMap.put(config, spec);
	}

	public static void start(ListenerConfig config) {

		if (listenersMap.containsKey(config)
				&& listenersMap.get(config).isConnected()) {
			return;
		} else {
			NioServerSocketChannelFactory factory = new NioServerSocketChannelFactory();
			ServerBootstrap bootstrap = new ServerBootstrap(factory);
			bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

				@Override
				public ChannelPipeline getPipeline() throws Exception {
					return (Channels.pipeline(new ListenerChannelHandler()));
				}

			});
			ChannelGroup grp = new DefaultChannelGroup();
			grp.add(bootstrap.bind(new InetSocketAddress(config.getPort())));
			channelGroupMap.put(config, grp);
			bootStrapMap.put(config, bootstrap);
			configs.add(config);
		}

	}

	public static void stop(ListenerConfig config) {
		try {
			channelGroupMap.get(config).close().await(10, TimeUnit.SECONDS);
			bootStrapMap.get(config).releaseExternalResources();
			channelGroupMap.remove(config);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void close(Channel channel) {
		listenersMap.remove(getConfigForChannel(channel));
	}

	public static void add(Channel channel) {
		listenersMap.put(getConfigForChannel(channel), channel);
	}

	public static ListenerConfig getConfigForChannel(Channel channel) {
		int port = ((InetSocketAddress) channel.getLocalAddress()).getPort();
		for (ListenerConfig config : configs) {
			if (port == config.getPort()) {
				return (config);
			}
		}
		return null;
	}

	public static boolean isOpen(ListenerConfig config) {
		return (channelGroupMap.containsKey(config));
	}

	public static Spec getSpecForConfig(ListenerConfig config) {
		return (specConfigMap.get(config));
	}

	public static void sendReply(Spec spec, MessageData msgData) {
		for (ListenerConfig config : specConfigMap.keySet()) {
			if (specConfigMap.get(config) == spec) {
				byte[] data = config.getMli().getMliImpl()
						.toNetwork(msgData.assemble(true));
				ChannelBuffer buffer = ChannelBuffers.copiedBuffer(data);
				listenersMap.get(config).write(buffer);
				break;
			}
		}

	}
}
