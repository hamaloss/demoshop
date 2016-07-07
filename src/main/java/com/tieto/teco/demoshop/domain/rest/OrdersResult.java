package com.tieto.teco.demoshop.domain.rest;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdersResult implements Serializable {
	private static final long serialVersionUID = -238907550469973701L;
	
	@JsonProperty
	private String email;
	
	@JsonProperty
	private List<Order> orders;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	
	
}
