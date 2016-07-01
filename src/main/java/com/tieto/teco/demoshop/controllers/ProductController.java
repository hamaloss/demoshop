package com.tieto.teco.demoshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tieto.teco.demoshop.domain.Product;
import com.tieto.teco.demoshop.services.ProductService;

@Controller
public class ProductController {
	private ProductService productService;
	
	@Autowired
	public void setProductService(ProductService p) {
		this.productService = p;
	}

	@RequestMapping("product/new")
	public String newProduct(Model model) {
		model.addAttribute("product", new Product());
		return "productform";
	}
	
	@RequestMapping(value = "product", method = RequestMethod.POST)
	public String saveProduct(Product p) {
		productService.saveProduct(p);
		return "redirect:/product/" + p.getId();
	}
	
	@RequestMapping("product/{id}")
	public String showProduct(@PathVariable Long id, Model model) {
		model.addAttribute("product", productService.getProductById(id));
		return "productshow";
	}
	
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("products", productService.listAllProducts());
		return "products";
	}
	
	@RequestMapping("product/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		model.addAttribute("product", productService.getProductById(id));
		return "productform";
	}
	
	@RequestMapping("product/delete/{id}")
	public String delete(@PathVariable Long id) {
		productService.deleteProduct(id);
		return "redirect:/products";
	}
}
