package com.tieto.teco.demoshop.services;

import java.util.Collection;

import com.tieto.teco.demoshop.domain.User;
import com.tieto.teco.demoshop.domain.UserCreateForm;

public interface UserService {
	User getUserById(long id);
	User getUserByEmail(String email);
	Collection<User> getAllUsers();
	User create(UserCreateForm form);
	User register(UserCreateForm form);
	User update(UserCreateForm form);
}
