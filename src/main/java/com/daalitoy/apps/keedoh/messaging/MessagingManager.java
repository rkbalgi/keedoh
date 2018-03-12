package com.daalitoy.apps.keedoh.messaging;

import com.daalitoy.apps.keedoh.data.model.Message;
import com.daalitoy.apps.keedoh.data.model.Spec;
import com.daalitoy.apps.keedoh.data.transit.MessageData;
import com.daalitoy.apps.keedoh.net.client.Connectors;
import com.daalitoy.apps.keedoh.net.server.Listeners;
import com.daalitoy.apps.keedoh.system.Properties;
import com.daalitoy.apps.keedoh.ui.util.Hex;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessagingManager {

    private static MessagingManager instance = new MessagingManager();

    private final int TIMEOUT_INTERVAL = Properties.getIntProperty(
            "MSG_TIMEOUT", 10);
    private Logger log = LogManager.getLogger(getClass());
    private ScheduledExecutorService service = Executors
            .newScheduledThreadPool(1);
    private Map<String, MessageData> flightMsgMap = Maps.newConcurrentMap();

    private MessagingManager() {
    }

    public static MessagingManager getInstance() {
        return (instance);
    }

    public MessageData dispatch(MessageData msgData)
            throws KeedohMessageTimeoutException {
        Connectors.register(msgData.getMessage().getConnectorConfig(), msgData
                .getMessage().getSpec());
        msgData.setProcessTime(new Date());

        Connectors.send(msgData);
        msgData.getLock().lock();
        try {
            msgData.getCondition().awaitUninterruptibly();
        } finally {
            msgData.getLock().unlock();
        }
        // if there is a response, we should have the entry in the message store
        MessageData rpMsgData = MessageStore.getClientResponseLeg(msgData
                .getCloneRef());
        if (rpMsgData == null) {
            throw new KeedohMessageTimeoutException();
        } else {
            return (rpMsgData);
        }

    }

    public void handleIncomingClientMessage(Spec spec, byte[] data) {
        log.debug(String.format("received msg for %s - %s", spec.getSpecName(),
                Hex.toString(data)));
        Message msg = MessageData.determineMessageType(spec, data);
        if (msg != null) {
            log.debug(String.format(
                    "[client] incoming message determined as [%s]",
                    msg.getMsgName()));
            MessageData msgData = MessageData.build(msg, Hex.toString(data),
                    false);
            msgData.setProcessTime(new Date());
            MessageData requestMsgData = removeFlightEntry(msgData);
            if (requestMsgData == null) {
                // timed out
                log.warn("message received possibly a late response.");
                MessageStore.logClient(requestMsgData, null);
            } else {
                // we have a request response pair, so let's log it
                MessageData cloneRef = MessageStore.logClient(requestMsgData,
                        msgData);
                requestMsgData.setCloneRef(cloneRef);
                // signal
                requestMsgData.getLock().lock();
                try {
                    requestMsgData.getCondition().signalAll();
                } finally {
                    requestMsgData.getLock().unlock();
                }

            }
        } else {
            log.error("unable to process incoming message - "
                    + Hex.toString(data));
        }

    }

    public void handleIncomingServerMessage(Spec spec, byte[] data,
                                            String scriptName) {
        log.debug(String.format("Received msg for %s - %s", spec.getSpecName(),
                Hex.toString(data)));
        Message msg = MessageData.determineMessageType(spec, data);
        if (msg != null) {
            log.debug(String.format(
                    "[server] incoming message determined as [%s]",
                    msg.getMsgName()));
            MessageData msgData = MessageData.build(msg, Hex.toString(data),
                    true);

            MessageData respMsgData = new MessageData(msgData.getMessage(),
                    false);
            MessagingUtils.merge(msgData, respMsgData);
            MessageHandlerHelper.handle(new SpecMsg(msgData, respMsgData),
                    scriptName);
            MessageStore.logServer(msgData, respMsgData);
            Listeners.sendReply(spec, respMsgData);
        } else {
            log.error("unable to process incoming message - "
                    + Hex.toString(data));
        }

    }

    public void makeFlightEntry(MessageData msgData) {

        String flightKey = MessagingUtils.buildFlightKey(msgData);
        log.debug(String.format("in-flight entry [%s]", flightKey));
        flightMsgMap.put(flightKey, msgData);
        service.schedule(new MessageTimeoutTask(msgData), TIMEOUT_INTERVAL,
                TimeUnit.SECONDS);
    }

    public MessageData removeFlightEntry(MessageData msgData) {
        String flightKey = MessagingUtils.buildFlightKey(msgData);
        log.debug(String.format("ex-flight entry [%s]", flightKey));

        return (flightMsgMap.remove(flightKey));

    }

}
