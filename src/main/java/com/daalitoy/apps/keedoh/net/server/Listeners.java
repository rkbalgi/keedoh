package com.daalitoy.apps.keedoh.net.server;

import com.daalitoy.apps.keedoh.data.model.ListenerConfig;
import com.daalitoy.apps.keedoh.data.model.Spec;
import com.daalitoy.apps.keedoh.data.transit.MessageData;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutor;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Listeners {

  private static final Logger log = LogManager.getLogger(Listeners.class);

  private static Map<ListenerConfig, Channel> listenersMap = Maps.newHashMap();
  private static Map<ListenerConfig, ChannelGroup> channelGroupMap = Maps.newHashMap();
  private static Map<ListenerConfig, ServerBootstrap> bootStrapMap = Maps.newHashMap();

  private static List<ListenerConfig> configs = Lists.newArrayList();
  private static Map<ListenerConfig, Spec> specConfigMap = Maps.newHashMap();

  public static void register(ListenerConfig config, Spec spec) {
    specConfigMap.put(config, spec);
  }

  public static void start(ListenerConfig config) {

    if (listenersMap.containsKey(config) && listenersMap.get(config).isOpen()) {
      return;
    } else {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.childHandler(new ListenerChannelHandler());
      bootstrap.channel(NioServerSocketChannel.class);
      bootstrap.group(new NioEventLoopGroup());
      bootstrap.childOption(ChannelOption.AUTO_READ, true);

      ChannelGroup grp = new DefaultChannelGroup(new DefaultEventExecutor());

      ChannelFuture future = bootstrap.bind(new InetSocketAddress(config.getPort()));
      Channel channel = future.syncUninterruptibly().channel();

      if (channel != null && log.isDebugEnabled()) {
        log.debug("TCP listener opened at port - " + config.getPort());
      }

      channelGroupMap.put(config, grp);
      bootStrapMap.put(config, bootstrap);
      configs.add(config);
    }
  }

  public static void stop(ListenerConfig config) {
    try {
      channelGroupMap.get(config).close().await(10, TimeUnit.SECONDS);
      bootStrapMap.get(config);
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
    int port = ((InetSocketAddress) channel.localAddress()).getPort();
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
        byte[] data = config.getMli().getMliImpl().toNetwork(msgData.assemble(false));
        ByteBuf buffer = Unpooled.copiedBuffer(data);
        log.debug("Sending reply - " + ByteBufUtil.prettyHexDump(buffer));
        listenersMap.get(config).writeAndFlush(buffer);
        break;
      }
    }
  }
}
