package com.daalitoy.apps.keedoh.net.server;

import com.daalitoy.apps.keedoh.data.model.ListenerConfig;
import com.daalitoy.apps.keedoh.data.model.Spec;
import com.daalitoy.apps.keedoh.messaging.MessagingManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ListenerChannelHandler extends ChannelDuplexHandler {

    private Logger log = LogManager.getLogger(getClass());

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error(cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);

        ListenerConfig config = Listeners.getConfigForChannel(ctx.channel());
        log.debug(config);
        ByteBuf buffer = (ByteBuf) msg;
        if (config.getMli().getMliImpl().packetExists(buffer)) {
            byte[] data = config.getMli().getMliImpl().fromNetwork(buffer);
            Spec spec = Listeners.getSpecForConfig(config);
            MessagingManager.getInstance()
                    .handleIncomingServerMessage(spec, data, config.getProcessorScript());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.debug(String.format("channel closed - %s", ctx.channel().remoteAddress()));
        Listeners.close(ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.debug(String.format("channel opened - %s", ctx.channel().remoteAddress()));
        Listeners.add(ctx.channel());
    }
}
