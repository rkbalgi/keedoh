package com.daalitoy.apps.keedoh.messaging;

import java.util.List;
import java.util.Map;

import com.daalitoy.apps.keedoh.data.transit.MessageData;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class MessageStore {

	private static Map<MessageData, MessageData> clientMsgMap = Maps
			.newHashMap();
	private static List<MessageData> clientMsgList = Lists.newArrayList();

	private static Map<MessageData, MessageData> serverMsgMap = Maps
			.newHashMap();
	private static List<MessageData> serverMsgList = Lists.newArrayList();

	public synchronized static MessageData logClient(MessageData rqMsgData,
			MessageData rpMsgData) {
		MessageData mData = (MessageData) rqMsgData.clone();
		clientMsgMap.put(mData, rpMsgData);
		clientMsgList.add(mData);
		return (mData);
	}

	public static MessageData getClientMsg(int index) {
		return (clientMsgList.get(index));
	}

	public static int getClientMsgStoreSize() {
		return clientMsgList.size();
	}

	// server related

	public synchronized static void logServer(MessageData rqMsgData,
			MessageData rpMsgData) {
		// MessageData mData = (MessageData) rqMsgData.clone();
		serverMsgMap.put(rqMsgData, rpMsgData);
		serverMsgList.add(rqMsgData);
	}

	public static MessageData getServerMsg(int index) {
		return (serverMsgList.get(index));
	}

	public static int getServerMsgStoreSize() {
		return serverMsgList.size();
	}

	/**
	 * return the response for the request represented by <code>msgData</code>
	 * 
	 * @param msgData
	 * @return
	 */
	public static MessageData getClientResponseLeg(MessageData msgData) {
		return (clientMsgMap.get(msgData));
	}
	
	/**
	 * return the response for the request represented by <code>msgData</code>
	 * 
	 * @param msgData
	 * @return
	 */
	public static MessageData getServerResponseLeg(MessageData msgData) {
		return (serverMsgMap.get(msgData));
	}

}
