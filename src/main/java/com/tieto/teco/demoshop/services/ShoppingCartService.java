package com.tieto.teco.demoshop.services;

import com.tieto.teco.demoshop.domain.ShoppingCart;
import com.tieto.teco.demoshop.domain.User;

public interface ShoppingCartService {
	public ShoppingCart getShoppingCartById(Long id);
	public ShoppingCart getShoppingCartByUser(User user);
	public ShoppingCart saveShoppingCart(ShoppingCart cart);
	public void deleteShoppingCart(Long id);
}
