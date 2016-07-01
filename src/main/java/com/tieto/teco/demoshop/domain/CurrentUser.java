package com.tieto.teco.demoshop.domain;

import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUser extends org.springframework.security.core.userdetails.User {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6362398480090538305L;
	private User user;
	
	public CurrentUser(User user) {
		super(user.getEmail(), user.getPasswordHash(), AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public Long getId() {
		return user.getId();
	}
	
	public boolean hasRole(Role role) {
		if(user.getRoles().contains(role.toString())) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
    public String toString() {
        return "CurrentUser{" +
                "user=" + user +
                "} " + super.toString();
    }
}
