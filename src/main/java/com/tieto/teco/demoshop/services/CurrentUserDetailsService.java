package com.tieto.teco.demoshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tieto.teco.demoshop.domain.CurrentUser;
import com.tieto.teco.demoshop.domain.User;

@Service
public class CurrentUserDetailsService implements UserDetailsService {
	private final UserService userService;
	
	@Autowired
    public CurrentUserDetailsService(UserService userService) {
        this.userService = userService;
    }

	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userService.getUserByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("User with email=%s was not found", email));
		}
		return new CurrentUser(user);
	}
	
	
}
