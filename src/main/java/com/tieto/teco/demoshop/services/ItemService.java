package com.tieto.teco.demoshop.services;

import com.tieto.teco.demoshop.domain.Item;

public interface ItemService {
	public Item getItemById(Long id);
	public Item saveItem(Item item);
	public void deleteItem(Long id);
}
