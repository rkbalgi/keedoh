package com.daalitoy.apps.keedoh.net.client;

import com.daalitoy.apps.keedoh.data.model.ConnectorConfig;
import com.daalitoy.apps.keedoh.data.model.MLI;
import com.daalitoy.apps.keedoh.data.model.Spec;
import com.daalitoy.apps.keedoh.data.transit.MessageData;
import com.daalitoy.apps.keedoh.messaging.MessagingManager;
import com.daalitoy.apps.keedoh.net.KeedohNetException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Connectors {

    private static Logger log = LogManager.getLogger(Connectors.class);
    private static Map<ConnectorConfig, Channel> connectorsMap = Maps.newHashMap();
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
        log.debug("Writing data " + ByteBufUtil.hexDump(packet));
        connectorsMap.get(config).writeAndFlush(Unpooled.copiedBuffer(packet));
    }

    public static void connect(ConnectorConfig config) {

        if (connectorsMap.containsKey(config) && connectorsMap.get(config).isOpen()) {
            return;
        } else {
            log.info("attempting to connect..");

            // NioClientSocketChannelFactory factory = new NioClientSocketChannelFactory();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ConnectorChannelHandler());
            bootstrap.group(new NioEventLoopGroup());

            ChannelFuture future =
                    bootstrap.connect(new InetSocketAddress(config.getIp(), config.getPort()));
            try {
                future.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.error(e);
            }
            if (future.isDone() && future.isSuccess()) {
                log.debug("connected to " + config.getIp() + ":" + config.getPort());
                configs.add(config);
                connectorsMap.put(config, future.channel());
            } else {
                throw new KeedohNetException("connect failed.");
            }
        }
    }

    public static void disconnect(ConnectorConfig connectorConfig) {
        try {
            connectorsMap.get(connectorConfig).close().await(10, TimeUnit.SECONDS);
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
        int port = ((InetSocketAddress) channel.remoteAddress()).getPort();
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
