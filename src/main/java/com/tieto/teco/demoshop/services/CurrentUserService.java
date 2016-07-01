package com.tieto.teco.demoshop.services;

import com.tieto.teco.demoshop.domain.CurrentUser;

public interface CurrentUserService {
	boolean canAccessUser(CurrentUser currentUser, Long userId);
}
