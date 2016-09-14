package com.daalitoy.apps.keedoh.data.model;

import java.util.Arrays;
import java.util.BitSet;

import com.daalitoy.apps.keedoh.ui.util.Hex;
import com.google.common.base.Preconditions;

public class Bit {
	// 1000 0000 0100 0000 001
	private static final byte[] LOOKUP = { (byte) 0x80, 0x40, 0x20, 0x10, 0x08,
			0x04, 0x02, 0x01 };
	private BitSet bitSet = new BitSet(129);

	public Bit(byte[] data) {
		Preconditions.checkArgument((data != null)
				&& ((data.length == 8) || (data.length == 16)));
		int n = 1;
		for (byte b : data) {
			for (int i = 7, j = 8 * n; i > 0; i--, j--) {
				if ((b & 0x01) == 0x01) {
					bitSet.set(j);
				}
				b = (byte) (b >> 1);
			}
			n++;
		}
	}

	public boolean isOn(int position) {
		Preconditions.checkArgument(position > 0 && position < 129);
		return (bitSet.get(position));
	}

	public byte[] getData() {

		byte[] newData = new byte[16];

		int n = 0;
		for (int i = 0; i < 16; i++) {
			for (int j = 0 + n, k = 0; j < 8 + n; j++, k++) {
				if (bitSet.get(j + 1)) {
					newData[i] = (byte) (newData[i] | LOOKUP[k]);
				}
			}
			n += 8;
		}

		if (Arrays.equals(Arrays.copyOfRange(newData, 8, 16), new byte[8])) {
			return (Arrays.copyOfRange(newData, 0, 8));
		}

		return (newData);
	}

	public void setOn(int position) {
		Preconditions.checkArgument(position > 0 && position < 129);
		bitSet.set(position, true);

	}

	public void setOff(int position) {
		Preconditions.checkArgument(position > 0 && position < 129);
		bitSet.set(position, false);

	}

	public static void main(String[] args) {
		Bit b = new Bit(new byte[] { 0x22, 0x22, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x03 });

		Preconditions.checkArgument(b.isOn(3) && b.isOn(7) && b.isOn(11)
				& b.isOn(15) && b.isOn(64));
		b.setOn(8);
		b.setOn(62);
		b.setOn(128);
		System.out.println(Hex.toString(b.getData()));
	}

}
