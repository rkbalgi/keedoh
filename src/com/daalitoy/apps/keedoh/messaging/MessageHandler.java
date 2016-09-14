package com.daalitoy.apps.keedoh.messaging;


public interface MessageHandler {
	
	public static final int REQUEST=0;
	public static final int RESPONSE=1;


	void handleMsg(SpecMsg msg);
}
