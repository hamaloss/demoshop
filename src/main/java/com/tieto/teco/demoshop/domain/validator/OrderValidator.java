package com.tieto.teco.demoshop.domain.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tieto.teco.demoshop.domain.rest.Order;

@Component
public class OrderValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		return arg0.equals(Order.class);
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		// TODO Auto-generated method stub
		
	}

}
