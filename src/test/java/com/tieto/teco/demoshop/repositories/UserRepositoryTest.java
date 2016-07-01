package com.tieto.teco.demoshop.repositories;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tieto.teco.demoshop.configuration.RepositoryConfiguration;
import com.tieto.teco.demoshop.domain.Role;
import com.tieto.teco.demoshop.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {RepositoryConfiguration.class})
public class UserRepositoryTest {
	private UserRepository userRepository;
	
	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Test
	public void testSaveUser() {
		User u = new User();
		u.setEmail("testi@testi.com");
		u.setPasswordHash("abcdefghijklmnopqrstu");
		u.addRole(Role.USER);
		assertNull(u.getId());
		userRepository.save(u);
		assertNotNull(u.getId());
		
		User u2 = userRepository.findOne(u.getId());
		assertNotNull(u2);
		assertEquals(u.getId(), u2.getId());
		assertEquals(u.getEmail(), u2.getEmail());
		
		u2.setPasswordHash("123456789");
		userRepository.save(u2);
		
		User u3 = userRepository.findOne(u2.getId());
		assertEquals(u2.getPasswordHash(), u3.getPasswordHash());
		
		long pCount = userRepository.count();
		assertEquals(pCount, 1);
		
		
	}
}
