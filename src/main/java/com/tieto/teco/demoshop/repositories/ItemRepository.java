package com.tieto.teco.demoshop.repositories;

import org.springframework.data.repository.CrudRepository;

import com.tieto.teco.demoshop.domain.Item;

public interface ItemRepository extends CrudRepository<Item, Long> {

}
