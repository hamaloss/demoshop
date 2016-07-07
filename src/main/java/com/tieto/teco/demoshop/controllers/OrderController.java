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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.tieto.teco.demoshop.common.ShopRestException;
import com.tieto.teco.demoshop.domain.OrderForm;
import com.tieto.teco.demoshop.domain.ShoppingCart;
import com.tieto.teco.demoshop.domain.User;
import com.tieto.teco.demoshop.domain.rest.Order;
import com.tieto.teco.demoshop.domain.rest.OrdersResult;
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
	
	@Value("${ORDER_HOST}")
	private String orderHost;
	
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
	
	@RequestMapping("order/list")
	public ModelAndView orderList(HttpServletRequest req, Principal principal) {
		String userName = principal.getName();
		RestTemplate restTemplate = new RestTemplate();
		String orderListUrl = "http://"+this.orderHost+"/report/api/v1.0/myorders/"+userName;
		Map<String, String> vars = new HashMap<String, String>();
		String queryId = UUID.randomUUID().toString();
		LOGGER.debug("Making http query to backend with queryid "+queryId);
		vars.put("id", queryId);
		
		OrdersResult result = restTemplate.getForObject(orderListUrl, OrdersResult.class, vars);
		
		return new ModelAndView("orders", "orders", result.getOrders());
	}
	
	@RequestMapping("order/details/{id}")
	public ModelAndView orderDetails(@PathVariable Long id, Principal principal) {
		String userName = principal.getName();
		RestTemplate restTemplate = new RestTemplate();
		String orderUrl = "http://"+this.orderHost+"/report/api/v1.0/order/"+id.toString();
		Map<String, String> vars = new HashMap<String, String>();
		String queryId = UUID.randomUUID().toString();
		LOGGER.debug("Making http query to backend with queryid "+queryId);
		vars.put("id", queryId);
		
		Order order = restTemplate.getForObject(orderUrl, Order.class, vars);
		LOGGER.debug("Username in order: "+order.getUserName());
		if(!order.getUserName().equals(userName)) {
			throw new ShopRestException("Not authorized", 404);
		} 
		return new ModelAndView("orderdetails", "order", order);
		
	}
	
	@RequestMapping(value = "order/create", method = RequestMethod.POST)
	public ModelAndView handleOrderForm(@Valid @ModelAttribute("order") OrderForm order, BindingResult bindingResult, HttpServletRequest req, Principal principal) {
		String orderUrl = "http://"+ this.orderHost + "/report/api/v1.0/neworder";
		LOGGER.debug("Order url: "+orderUrl);
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
		String queryId = UUID.randomUUID().toString();
		vars.put("id", queryId);
		vars.put("username", userName);
		Order o = new Order(order, cart.getItems());
		o.setUserName(userName);
		String orderResult = null;
		LOGGER.debug("Making http post to backend with queryid "+queryId);
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
