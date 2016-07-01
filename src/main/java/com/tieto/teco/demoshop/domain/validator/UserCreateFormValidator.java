package com.tieto.teco.demoshop.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tieto.teco.demoshop.domain.UserCreateForm;
import com.tieto.teco.demoshop.services.UserService;

@Component
public class UserCreateFormValidator implements Validator {
	private final UserService userService;
	
	@Autowired
    public UserCreateFormValidator(UserService userService) {
        this.userService = userService;
    }
	
	public boolean supports(Class<?> clazz) {
		return clazz.equals(UserCreateForm.class);
	}
	
	public void validate(Object target, Errors errors) {
		UserCreateForm form = (UserCreateForm) target;
		validatePasswords(errors, form);
		validateEmail(errors, form);
	}
	
	private void validatePasswords(Errors errors, UserCreateForm form) {
		if (!form.getPassword().equals(form.getPasswordRepeated())) {
			errors.reject("password.no_match", "Passwords do not match");
		}
	}
	
	private void validateEmail(Errors errors, UserCreateForm form) {
		if(userService.getUserByEmail(form.getEmail()) != null) {
			errors.reject("email.exists", "User with this email already exists");
		}
	}
}
