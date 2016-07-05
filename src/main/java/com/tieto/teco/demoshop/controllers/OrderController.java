package com.tieto.teco.demoshop.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.tieto.teco.demoshop.common.ShopRestException;
import com.tieto.teco.demoshop.domain.OrderForm;
import com.tieto.teco.demoshop.domain.ShoppingCart;
import com.tieto.teco.demoshop.domain.User;
import com.tieto.teco.demoshop.domain.rest.Order;
import com.tieto.teco.demoshop.services.ShoppingCartService;
import com.tieto.teco.demoshop.services.UserService;

@Controller
public class OrderController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
	private ShoppingCartService cartService;
	private UserService userService;
	
	@Autowired
	public void setShoppingCartService(ShoppingCartService s) {
		this.cartService = s;
	}
	
	@Autowired
	public void setUserService(UserService u) {
		this.userService = u;
	}
	
	@RequestMapping("order/view")
	public ModelAndView orderCart(HttpServletRequest req, Principal principal) {
		HttpSession session = req.getSession();
		ShoppingCart cart = null;
		String userName = principal.getName();
		LOGGER.debug("User "+userName+" trying to make order");
		User u = userService.getUserByEmail(userName);
		Long cartId = (Long)session.getAttribute("cartid");
		if(cartId != null) {
			cart = cartService.getShoppingCartById(cartId);
		} else {
			throw new ShopRestException("No shopping cart found", 400);
		}
		OrderForm order = new OrderForm();
		order.setItems(cart.getItems());
		order.setUserName(u.getEmail());
		
		return new ModelAndView("order", "order", order);
	}
	
	@RequestMapping(value = "order/create", method = RequestMethod.POST)
	public ModelAndView handleOrderForm(@Valid @ModelAttribute("order") OrderForm order, BindingResult bindingResult, HttpServletRequest req, Principal principal) {
		String orderUrl = System.getenv("ORDER_URL");
		HttpSession session = req.getSession();
		ShoppingCart cart = null;
		String userName = principal.getName();
		Long cartId = (Long)session.getAttribute("cartid");
		if(cartId != null) {
			cart = cartService.getShoppingCartById(cartId);
		} else {
			throw new ShopRestException("No shopping cart found", 400);
		}
		RestTemplate restTemplate = new RestTemplate();
		if (bindingResult.hasErrors()) {
			order.setItems(cart.getItems());
			return new ModelAndView("order", "order", order);
		}

		Map<String, String> vars = new HashMap<String, String>();
		vars.put("id", UUID.randomUUID().toString());
		vars.put("username", userName);
		Order o = new Order(order, cart.getItems());
		o.setUserName(userName);
		String orderResult = null;
		ResponseEntity<String> response = restTemplate.postForEntity(
				orderUrl, o, String.class, vars);
		if (response.getStatusCode().is2xxSuccessful()) {
			LOGGER.info("Order sent. Message: "+response.getBody());
			orderResult = "Order successfully sent";
			LOGGER.info("Deleting shopping cart");
			cartService.deleteShoppingCart(cart.getId());
			session.setAttribute("cartid", null);
		} else {
			LOGGER.error("Error sending order. Satus code: "
					+ response.getStatusCode().toString() + " message: "
					+ response.getBody());
			throw new ShopRestException(
					"Error sending order to backend system", 500);
		}

		return new ModelAndView("order_sent", "result", orderResult);
	}
	
}
