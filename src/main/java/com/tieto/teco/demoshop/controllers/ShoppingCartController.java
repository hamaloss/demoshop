package com.tieto.teco.demoshop.controllers;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tieto.teco.demoshop.common.ShopRestException;
import com.tieto.teco.demoshop.domain.Item;
import com.tieto.teco.demoshop.domain.Product;
import com.tieto.teco.demoshop.domain.ShoppingCart;
import com.tieto.teco.demoshop.domain.User;
import com.tieto.teco.demoshop.services.ProductService;
import com.tieto.teco.demoshop.services.ShoppingCartService;
import com.tieto.teco.demoshop.services.UserService;

@Controller
public class ShoppingCartController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartController.class);
	private ShoppingCartService cartService;
	private ProductService productService;
	private UserService userService;
	
	@Autowired
	public void setShoppingCartService(ShoppingCartService s) {
		this.cartService = s;
	}
	
	@Autowired
	public void setProductService(ProductService p) {
		this.productService = p;
	}
	
	@Autowired
	public void setUserService(UserService u) {
		this.userService = u;
	}
	
	@RequestMapping("cart/view")
	public String viewCart(HttpServletRequest req, Principal principal, Model model) {
		HttpSession session = req.getSession();
		ShoppingCart cart = null;
		String userName = principal.getName();
		LOGGER.debug("User "+userName+" trying to view cart");
		User u = userService.getUserByEmail(userName);
		Long cartId = (Long)session.getAttribute("cartid");
		LOGGER.debug("Cartid from session: "+cartId);
		if(cartId == null) {
			LOGGER.debug("No card id in session, lets check if user has cart in database");
			cart = cartService.getShoppingCartByUser(u);
			if(cart == null) {
				LOGGER.debug("No shopping cart found from database, creating new one");
				cart = new ShoppingCart();
				cart.setUser(u);
				cartService.saveShoppingCart(cart);
			}
			LOGGER.debug("Adding shopping cart id to session");
			session.setAttribute("cartid", cart.getId());
		} else {
			cart = cartService.getShoppingCartById(cartId);
		}
		model.addAttribute("cart", cart);
		return "shoppingcart";
	}
	
	@RequestMapping("cart/add/{id}")
	public String addToCart(@PathVariable Long id,@RequestParam(required = false) Integer count, HttpServletRequest req, Principal principal, Model model) {
		HttpSession session = req.getSession();
		ShoppingCart cart = null;
		String userName = principal.getName();
		LOGGER.debug("User "+userName+" trying to add item to cart");
		User u = userService.getUserByEmail(userName);
		Long cartId = (Long)session.getAttribute("cartid");
		LOGGER.debug("Cartid from session: "+cartId);
		if(cartId == null) {
			LOGGER.debug("No card id in session, lets check if user has cart in database");
			cart = cartService.getShoppingCartByUser(u);
			if(cart == null) {
				LOGGER.debug("No shopping cart found from database, creating new one");
				cart = new ShoppingCart();
				cart.setUser(u);
				cartService.saveShoppingCart(cart);
			}
			LOGGER.debug("Adding shopping cart id to session");
			session.setAttribute("cartid", cart.getId());
		} else {
			cart = cartService.getShoppingCartById(cartId);
		}
		Product p = productService.getProductById(id);
		Item item = new Item();
		item.setProduct(p);
		if(count != null && count > 0) {
			item.setCount(count);
		} else {
			item.setCount(1);
		}
		Item temp = cart.addItem(item);
		LOGGER.debug("Got item with count "+temp.getCount()+" from addItem");
		cartService.saveShoppingCart(cart);
		model.addAttribute("cart", cart);
		return "shoppingcart";
	}
	
	@RequestMapping("cart/delete")
	public String deleteCart(HttpServletRequest req, Principal principal) {
		HttpSession session = req.getSession();
		ShoppingCart cart = null;
		String userName = principal.getName();
		LOGGER.debug("User "+userName+" trying to delete cart");
	//	User u = userService.getUserByEmail(userName);
		Long cartId = (Long)session.getAttribute("cartid");
		if(cartId != null) {
			cart = cartService.getShoppingCartById(cartId);
			if(cart != null) {
				cartService.deleteShoppingCart(cart.getId());
			}
			session.setAttribute("cartid", null);
		}
		
		return "redirect:/cart/view";
	}
	
	
}
