package com.daalitoy.apps.keedoh.ui.util;

import io.netty.buffer.ByteBufUtil;

public class Hex {

  public static String toString(byte[] data) {
    return ByteBufUtil.hexDump(data);
  }

  public static byte[] fromString(String hexStr) {
    return ByteBufUtil.decodeHexDump(hexStr);
  }
}
