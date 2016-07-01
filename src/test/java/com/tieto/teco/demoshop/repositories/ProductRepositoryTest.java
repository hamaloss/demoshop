package com.tieto.teco.demoshop.repositories;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tieto.teco.demoshop.configuration.RepositoryConfiguration;
import com.tieto.teco.demoshop.domain.Product;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {RepositoryConfiguration.class})
public class ProductRepositoryTest {
	private ProductRepository productRepository;
	
	@Autowired
	public void setProductRepository(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	@Test
	public void testSaveProduct() {
		Product p = new Product();
		p.setName("Testituote");
		p.setDescription("Tämä on hieno testituote");
		p.setPrice(23.45);
		p.setProductId("ABCD1");
		
		assertNull(p.getId());
		productRepository.save(p);
		assertNotNull(p.getId());
		
		Product p2 = productRepository.findOne(p.getId());
		assertNotNull(p2);
		assertEquals(p.getId(), p2.getId());
		assertEquals(p.getName(), p2.getName());
		
		p2.setDescription("Parempi kuvaus");
		productRepository.save(p2);
		
		Product p3 = productRepository.findOne(p2.getId());
		assertEquals(p2.getDescription(), p3.getDescription());
		
		long pCount = productRepository.count();
		assertEquals(pCount, 1);
		
		
	}
}
