package com.tieto.teco.demoshop.common;

public class ShopRestException extends RuntimeException {
	private static final long serialVersionUID = -6058220608049974110L;
	int statusCode;
	
	public ShopRestException(String message, int statusCode) {
		super(message);
		this.statusCode = statusCode;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
