package com.tieto.teco.demoshop.controllers;

import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tieto.teco.demoshop.domain.Role;
import com.tieto.teco.demoshop.domain.User;
import com.tieto.teco.demoshop.domain.UserCreateForm;
import com.tieto.teco.demoshop.domain.validator.UserCreateFormValidator;
import com.tieto.teco.demoshop.services.UserService;

@Controller
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	private final UserService userService;
	private final UserCreateFormValidator userCreateFormValidator;
	
	@Autowired
    public UserController(UserService userService, UserCreateFormValidator userCreateFormValidator) {
        this.userService = userService;
        this.userCreateFormValidator = userCreateFormValidator;
    }
	
	@InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCreateFormValidator);
    }
	
	@RequestMapping("/user/{id}")
    public ModelAndView getUserPage(@PathVariable Long id) {
		User user = userService.getUserById(id);
		if(user == null) {
			throw new NoSuchElementException(String.format("User=%s not found", id));
		}
        return new ModelAndView("user", "user", user);
    }
	
	@RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public ModelAndView getUserCreatePage() {
        return new ModelAndView("user_create", "form", new UserCreateForm());
    }
	
	@RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String handleUserCreateForm(@Valid @ModelAttribute("form") UserCreateForm form, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "user_create";
		}
		try {
			userService.create(form);
		} catch (DataIntegrityViolationException e) {
			bindingResult.reject("email.exists","Email already exists");
			return "user_create";
		}
		return "redirect:/users";
	}
	
	@RequestMapping(value = "/user/edit/{id}", method = RequestMethod.GET)
	public ModelAndView getEditUserPage(@PathVariable Long id) {
		LOGGER.debug("Editing user id: "+id.toString());
		User user = userService.getUserById(id);
		UserCreateForm form = new UserCreateForm();
		form.setId(user.getId());
		if(user.hasRole(Role.ADMIN)) {
			form.setAdminRole(true);
		}
		if(user.hasRole(Role.USER)) {
			form.setUserRole(true);
		}
		form.setEmail(user.getEmail());
		LOGGER.debug("Returning model and view");
		return new ModelAndView("user_edit", "form", form);
	}
	
	@RequestMapping(value = "/user/edit/{id}", method = RequestMethod.POST)
	public String editUser(@ModelAttribute("form") UserCreateForm form, @PathVariable Long id) {
		form.setId(id);
		userService.update(form);
		return "redirect:/users";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView getRegisterPage() {
		return new ModelAndView("register", "form", new UserCreateForm());
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String handleRegisterForm(@Valid @ModelAttribute("form") UserCreateForm form, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "register";
		}
		try {
			userService.register(form);
		} catch (DataIntegrityViolationException e) {
			bindingResult.reject("email.exists","Email already exists");
			return "register";
		}
		return "redirect:/";
	}
}
