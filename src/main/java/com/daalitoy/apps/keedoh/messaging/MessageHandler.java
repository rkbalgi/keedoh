package com.daalitoy.apps.keedoh.messaging;


public interface MessageHandler {

    int REQUEST = 0;
    int RESPONSE = 1;


    void handleMsg(SpecMsg msg);
}
