package org.apache.commons.httpclient.methods;

import org.apache.commons.httpclient.methods.PostMethod;

public class UTF8PostMethod extends PostMethod {
	public UTF8PostMethod(String url) {
		super(url);
	}

	@Override
	public String getRequestCharSet() {
		// return super.getRequestCharSet();
		return "UTF-8";
	}
}
