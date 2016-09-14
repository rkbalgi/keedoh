package com.daalitoy.apps.keedoh.data.util;

public interface Charset {

	public String toString(byte[] data);
	public byte[] fromString(String stringData);
	public int toInt(byte[] data);
}
