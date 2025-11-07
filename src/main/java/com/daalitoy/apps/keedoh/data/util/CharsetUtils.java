package com.daalitoy.apps.keedoh.data.util;

import com.daalitoy.apps.keedoh.data.common.ENCODING_TYPE;
import com.daalitoy.apps.keedoh.ui.util.Hex;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Map;

public class CharsetUtils {

  private static Map<ENCODING_TYPE, Charset> charsetsMap = Maps.newHashMap();

  static {
    charsetsMap.put(ENCODING_TYPE.ASCII, new ASCIICharset());
    charsetsMap.put(ENCODING_TYPE.EBCDIC, new EbcdicCharset());
    charsetsMap.put(ENCODING_TYPE.BCD, new BCDCharset());
    charsetsMap.put(ENCODING_TYPE.BINARY, new BinaryCharset());
  }

  public static String toString(ENCODING_TYPE encodingType, byte[] data) {
    return (charsetsMap.get(encodingType).toString(data));
  }

  public static byte[] fromString(ENCODING_TYPE encodingType, String stringData) {
    return (charsetsMap.get(encodingType).fromString(stringData));
  }

  public static int toInt(ENCODING_TYPE encodingType, byte[] data) {
    int tmp = charsetsMap.get(encodingType).toInt(data);
    return (tmp);
  }
}

class ASCIICharset implements Charset {
  CharsetDecoder decoder = Charsets.US_ASCII.newDecoder();
  CharsetEncoder encoder = Charsets.US_ASCII.newEncoder();

  public String toString(byte[] data) {

    try {
      return (decoder.decode(ByteBuffer.wrap(data)).toString());
    } catch (CharacterCodingException e) {
      throw new RuntimeException(e);
    }
  }

  public byte[] fromString(String stringData) {
    try {
      return (encoder.encode(CharBuffer.wrap(stringData)).array());
    } catch (CharacterCodingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public int toInt(byte[] data) {
    return (Integer.parseInt(toString(data)));
  }
}

class EbcdicCharset implements Charset {

  public String toString(byte[] data) {

    try {
      return (new String(data, "cp037"));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public byte[] fromString(String stringData) {
    try {
      return (stringData.getBytes("cp037"));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public int toInt(byte[] data) {
    return (Integer.parseInt(toString(data)));
  }
}

class BCDCharset implements Charset {

  public String toString(byte[] data) {
    return (Hex.toString(data));
  }

  public byte[] fromString(String stringData) {
    return (Hex.fromString(stringData));
  }

  @Override
  public int toInt(byte[] data) {
    return (Integer.parseInt(toString(data)));
  }
}

class BinaryCharset implements Charset {

  public String toString(byte[] data) {
    return (Hex.toString(data));
  }

  public byte[] fromString(String stringData) {
    return (Hex.fromString(stringData));
  }

  @Override
  public int toInt(byte[] data) {
    return (Integer.parseInt(toString(data), 16));
  }
}
