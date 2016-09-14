package com.daalitoy.apps.keedoh.ui.util;

public class Hex {

	public static String toString(byte[] data) {
		return (new String(org.bouncycastle.util.encoders.Hex.encode(data))
				.toUpperCase());
	}

	public static byte[] fromString(String hexStr) {
		return (org.bouncycastle.util.encoders.Hex.decode(hexStr));
	}

}
