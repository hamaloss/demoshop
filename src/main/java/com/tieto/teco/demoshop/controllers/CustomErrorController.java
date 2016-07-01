package com.tieto.teco.demoshop.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomErrorController implements ErrorController {
	private static final String PATH = "/error";
	
	public String getErrorPath() {
		return PATH;
	}
	
	@RequestMapping(value = PATH)
	public ModelAndView error(HttpServletResponse response) {
		return new ModelAndView("error", "error_code", response.getStatus());
	}

}
