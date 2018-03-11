package com.daalitoy.apps.keedoh.net.client;

import com.daalitoy.apps.keedoh.data.model.ConnectorConfig;
import com.daalitoy.apps.keedoh.data.model.Spec;
import com.daalitoy.apps.keedoh.messaging.MessagingManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectorChannelHandler extends ChannelDuplexHandler {

    private static final Logger log = LogManager.getLogger(ConnectorChannelHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error(cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        ByteBuf buffer = (ByteBuf) msg;
        log.debug("msg received with readable bytes -" + buffer.readableBytes());
        ConnectorConfig config = Connectors.getConfigForChannel(ctx.channel());
        Spec spec = Connectors.getSpecForConfig(config);

        if (config.getMli().getMliImpl().packetExists(buffer)) {
            byte[] data = config.getMli().getMliImpl().fromNetwork(buffer);

            // this is a connector setup, so lets check if this a response
            // to an request

            MessagingManager.getInstance().handleIncomingClientMessage(spec, data);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
            throws Exception {
        super.write(ctx, msg, promise);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.debug(String.format("channel opened - %s", ctx.channel().remoteAddress()));
        Connectors.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.debug(String.format("channel closed - %s", ctx.channel().remoteAddress()));
        Connectors.close(ctx.channel());
    }
}
