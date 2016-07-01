package com.tieto.teco.demoshop.services;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tieto.teco.demoshop.domain.Role;
import com.tieto.teco.demoshop.domain.User;
import com.tieto.teco.demoshop.domain.UserCreateForm;
import com.tieto.teco.demoshop.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	private final UserRepository userRepository;
	
	@Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
	public User getUserById(long id) {
		return userRepository.findOne(id);
	}

	public User getUserByEmail(String email) {
		return userRepository.findOneByEmail(email);
	}

	public Collection<User> getAllUsers() {
		return userRepository.findAll(new Sort("email"));
	}

	public User create(UserCreateForm form) {
		User user = new User();
		user.setEmail(form.getEmail());
		user.setPasswordHash(new BCryptPasswordEncoder().encode(form.getPassword()));
		if(form.isAdminRole()) {
			user.addRole(Role.ADMIN);
		}
		user.addRole(Role.USER);

		return userRepository.save(user);
	}

	public User register(UserCreateForm form) {
		User user = new User();
		user.setEmail(form.getEmail());
		user.setPasswordHash(new BCryptPasswordEncoder().encode(form.getPassword()));
		
		return userRepository.save(user);
	}

	public User update(UserCreateForm form) {
		LOGGER.info("Update user id: "+form.getId().toString());
		User user = getUserById(form.getId());
		if(user == null) {
			LOGGER.error("User not found");
		}
 		user.setEmail(form.getEmail());
		if(form.isAdminRole()) {
			LOGGER.debug("Adding role ADMIN");
			user.addRole(Role.ADMIN);
		} else {
			LOGGER.debug("Removing role ADMIN");
			user.removeRole(Role.ADMIN);
		}
		if(form.isUserRole()) {
			LOGGER.debug("Adding role USER");
			user.addRole(Role.USER);
		} else {
			LOGGER.debug("Removing role USER");
			user.removeRole(Role.USER);
		}
		return userRepository.save(user);
	}

}
