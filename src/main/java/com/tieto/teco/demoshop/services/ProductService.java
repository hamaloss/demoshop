package com.tieto.teco.demoshop.services;

import com.tieto.teco.demoshop.domain.Product;

public interface ProductService {
	Iterable<Product> listAllProducts();
	
	Product getProductById(Long id);
	
	Product saveProduct(Product p);
	
	void deleteProduct(Long id);
}
