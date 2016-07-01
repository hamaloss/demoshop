package com.tieto.teco.demoshop.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;


@Entity
@Table(name = "shopping_cart")
public class ShoppingCart implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9028433968673463211L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Version
	private Integer version;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name ="user_id")
	private User user;
	
	@OneToMany(mappedBy="cart")
	@Cascade({CascadeType.ALL})
	private Set<Item> items;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}
	
	public Item addItem(Item item) {
		if(items == null) {
			items = new HashSet<>();
		}
		boolean itemFound = false;
		Iterator<Item> i = items.iterator();
		while(i.hasNext()) {
			Item it = i.next();
			if(it.getProduct() == item.getProduct()) {
				it.setCount(it.getCount()+item.getCount());
				item = it;
				itemFound = true;
				break;
			}
		}
		if(itemFound == false) {
			getItems().add(item);
			item.setCart(this);
		}
		
		return item;
	}
	
	
}
