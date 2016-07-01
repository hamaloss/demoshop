package com.tieto.teco.demoshop.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tieto.teco.demoshop.domain.ShoppingCart;
import com.tieto.teco.demoshop.domain.User;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {
	@Query("select s from ShoppingCart s where s.user = ?1")
	ShoppingCart findShoppingCartByUser(User user);
}
