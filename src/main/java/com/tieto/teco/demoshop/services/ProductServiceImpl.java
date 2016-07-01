package com.tieto.teco.demoshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tieto.teco.demoshop.domain.Product;
import com.tieto.teco.demoshop.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
	private ProductRepository productRepository;
	
	@Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

	public Iterable<Product> listAllProducts() {
		return productRepository.findAll(sortByIdAsc());
	}

	private Sort sortByIdAsc() {
		return new Sort(Sort.Direction.ASC, "id");
	}

	public Product getProductById(Long id) {
		return productRepository.findOne(id);
	}

	public Product saveProduct(Product p) {
		return productRepository.save(p);
	}

	public void deleteProduct(Long id) {
		productRepository.delete(id);
	}

}
