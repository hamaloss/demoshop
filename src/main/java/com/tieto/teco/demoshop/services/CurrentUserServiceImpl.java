package com.tieto.teco.demoshop.services;

import com.tieto.teco.demoshop.domain.CurrentUser;
import com.tieto.teco.demoshop.domain.Role;

public class CurrentUserServiceImpl implements CurrentUserService {

	public boolean canAccessUser(CurrentUser currentUser, Long userId) {
		return currentUser != null && (currentUser.hasRole(Role.ADMIN) || currentUser.getId().equals(userId));
	}

}
