package com.tieto.teco.demoshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tieto.teco.demoshop.domain.ShoppingCart;
import com.tieto.teco.demoshop.domain.User;
import com.tieto.teco.demoshop.repositories.ShoppingCartRepository;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
	private ShoppingCartRepository shoppingCartRepository;
	
	@Autowired
    public void setShoppingCartRepository(ShoppingCartRepository s) {
		this.shoppingCartRepository = s;
	}
	public ShoppingCart getShoppingCartById(Long id) {
		return shoppingCartRepository.findOne(id);
	}

	public ShoppingCart saveShoppingCart(ShoppingCart cart) {
		return shoppingCartRepository.save(cart);
	}

	public void deleteShoppingCart(Long id) {
		shoppingCartRepository.delete(id);
	}
	@Override
	public ShoppingCart getShoppingCartByUser(User user) {
		return shoppingCartRepository.findShoppingCartByUser(user);
	}

}
