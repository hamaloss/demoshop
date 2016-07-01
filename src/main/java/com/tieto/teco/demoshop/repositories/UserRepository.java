package com.tieto.teco.demoshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tieto.teco.demoshop.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findOneByEmail(String email);
}
