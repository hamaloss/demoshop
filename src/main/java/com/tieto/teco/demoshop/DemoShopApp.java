package com.tieto.teco.demoshop;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

//@EnableRedisHttpSession
@SpringBootApplication
public class DemoShopApp implements CommandLineRunner {
	
	@Autowired
	private DataSource dataSource;
	
//	@Autowired
//	@Qualifier("main_app-redis")
//	RedisConnectionFactory rcf;
	
	public void run(String... args) throws Exception {
		org.apache.tomcat.jdbc.pool.DataSource tomcat = (org.apache.tomcat.jdbc.pool.DataSource) dataSource;
		System.err.println(tomcat.getDriverClassName() + ", " + tomcat.getUrl());
		
	}
	
	

	public static void main(String[] args) {
		SpringApplication.run(DemoShopApp.class, args);
	}

}
