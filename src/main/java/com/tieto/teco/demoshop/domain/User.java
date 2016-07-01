package com.tieto.teco.demoshop.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "user")
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6413502794982809337L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;
	
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	
	@Column(name = "password_hash", nullable = false)
	private String passwordHash;
	
	@Column(name = "roles", nullable = true)
	private String roles;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getRoles() {
		if(roles == null) {
			return "";
		}
		return roles;
	}
	
	public void setRoles(String roles) {
		this.roles = roles;
	}

	public boolean hasRole(Role role) {
		if(roles != null) {
			if(roles.contains(role.toString())) {
				return true;
			}
		}
		return false;
	}
	
	public void addRole(Role role) {
		if(roles == null) {
			roles = role.toString();
		} else {
			if(roles.contains(role.toString())){
				//do nothing, no need to add the same role again.
			} else{
				List<String> roleList = new ArrayList<String>(Arrays.asList(roles.split(",")));
				System.out.println("Adding new role: "+role.toString());
				roleList.add(role.toString());
				roles = StringUtils.join(roleList, ',');
			}
		}
	}
	
	public void removeRole(Role role) {
		if(roles != null) {
			if(roles.contains(role.toString())) {
				List<String> roleList = new ArrayList<String>(Arrays.asList(roles.split(",")));
				roleList.remove(role.toString());
				if(roleList.size() > 0) {
					roles = StringUtils.join(roleList, ',');
				} else {
					roles = null;
				}
			}
		}
	}
	
	
}
