package com.tieto.teco.demoshop.repositories;


import org.springframework.data.repository.PagingAndSortingRepository;

import com.tieto.teco.demoshop.domain.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long>{
	
}
