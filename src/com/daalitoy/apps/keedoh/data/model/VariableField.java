package com.daalitoy.apps.keedoh.data.model;

import com.daalitoy.apps.keedoh.data.common.ENCODING_TYPE;

public class VariableField extends Field {

	protected int indicatorLength;
	protected ENCODING_TYPE indicatorEncodingType;

	public int getIndicatorLength() {
		return indicatorLength;
	}

	public void setIndicatorLength(int indicatorLength) {
		this.indicatorLength = indicatorLength;
	}

	public ENCODING_TYPE getIndicatorEncodingType() {
		return indicatorEncodingType;
	}

	public void setIndicatorEncodingType(ENCODING_TYPE indicatorEncodingType) {
		this.indicatorEncodingType = indicatorEncodingType;
	}

}
