package com.tieto.teco.demoshop.domain.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tieto.teco.demoshop.domain.Item;
import com.tieto.teco.demoshop.domain.OrderForm;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order implements Serializable {
	
	private static final long serialVersionUID = 7375871958602648420L;
	
	@JsonProperty
	private String userName;
	
	@JsonProperty
	private String name;
	
	@JsonProperty
	private String streetAddress;
	
	@JsonProperty
	private String postNumber;
	
	@JsonProperty
	private String city;
	
	@JsonProperty
	private List<Hashtable<String,Object>> items;
	
	private Integer orderid;
	private String orderStatus;
	
	public Order() {
		super();
	}
	
	public Order(Integer orderId, String orderStatus, List<Hashtable<String,Object>> items) {
		this.orderid = orderId;
		this.orderStatus = orderStatus;
		this.items = items;
	}
	
	public Order(OrderForm o, Set<Item> items) {
		this.userName = o.getUserName();
		this.name = o.getName();
		this.streetAddress = o.getStreetAddress();
		this.postNumber = o.getPostNumber();
		this.city = o.getCity();
		List<Hashtable<String,Object>> a = new ArrayList<Hashtable<String,Object>>();
		Iterator<Item> i = items.iterator();
		while(i.hasNext()) {
			Item item = i.next();
			Hashtable<String,Object> t = new Hashtable<String,Object>();
			t.put("count", item.getCount());
			t.put("productid", item.getProduct().getId());
			t.put("productname", item.getProduct().getName());
			t.put("productprice", item.getProduct().getPrice());
			a.add(t);
		}
		this.items = a;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getPostNumber() {
		return postNumber;
	}

	public void setPostNumber(String postNumber) {
		this.postNumber = postNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<Hashtable<String,Object>> getItems() {
		return items;
	}

	public void setItems(List<Hashtable<String,Object>> items) {
		this.items = items;
	}

	public Integer getOrderid() {
		return orderid;
	}

	public void setOrderid(Integer id) {
		this.orderid = id;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	
}
