package com.daalitoy.apps.keedoh.data.util;

public class Bits {

    public static boolean highBitOn(byte b) {
        return ((b & 0x80) == 0x80);
    }
}
