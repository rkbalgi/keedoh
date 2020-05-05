package com.daalitoy.apps.keedoh.data.util;

public interface Charset {

    String toString(byte[] data);

    byte[] fromString(String stringData);

    int toInt(byte[] data);
}
