package com.tieto.teco.demoshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tieto.teco.demoshop.domain.Item;
import com.tieto.teco.demoshop.repositories.ItemRepository;

@Service
public class ItemServiceImpl implements ItemService {
	private ItemRepository itemRepository;
	
	@Autowired
    public void setItemRepository(ItemRepository i) {
		this.itemRepository = i;
	}
	
	public Item getItemById(Long id) {
		return itemRepository.findOne(id);
	}

	public Item saveItem(Item item) {
		return itemRepository.save(item);
	}

	public void deleteItem(Long id) {
		itemRepository.delete(id);
	}

}
