package com.increff.pos.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;

@Service
public class UserService {

	@Autowired
	private UserDao dao;

	@Value("${supervisor.email}")
	private String supervisorEmail;

	@Transactional
	public void add(UserPojo p) throws ApiException {
		normalize(p);
		UserPojo existing = dao.select(p.getEmail());
		if (existing != null) {
			throw new ApiException("User with given email already exists");
		}
		p = assignRole(p);
		dao.insert(p);
	}

	@Transactional(rollbackOn = ApiException.class)
	public UserPojo get(String email) throws ApiException {
		return dao.select(email);
	}

	@Transactional
	public List<UserPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}

	protected static void normalize(UserPojo p) {
		p.setEmail(p.getEmail().toLowerCase().trim());
		p.setRole(p.getRole().toLowerCase().trim());
	}

	private UserPojo assignRole(UserPojo userPojo){
		List<String> supervisorEmailList = Arrays.asList(supervisorEmail.split("\\s*,\\s*"));
		for(String email : supervisorEmailList){
			if(userPojo.getEmail().equals(email)){
				userPojo.setRole("admin");
				break;
			}
		}
		return userPojo;
	}

}
