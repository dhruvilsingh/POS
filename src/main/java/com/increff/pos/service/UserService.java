package com.increff.pos.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;

import com.increff.pos.model.enums.Role;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserDao userDao;

	@Value("${supervisor.email}")
	private String supervisorEmail;

	public void add(UserPojo p) throws ApiException {
		normalize(p);
		UserPojo existing = userDao.select(p.getEmail());
		if (existing != null) {
			throw new ApiException("User with entered email already exists!");
		}
		p = assignRole(p);
		userDao.insert(p);
	}

	public UserPojo get(String email) throws ApiException {
		return userDao.select(email);
	}

	public UserPojo getId(int id){
		return userDao.selectId(id);
	}

	public List<UserPojo> getAll() {
		return userDao.selectAll();
	}

	public void delete(int id) throws ApiException {
		if(getId(id).getRole() == Role.ADMIN){
			throw new ApiException("Cannot delete an Admin!");
		}
		userDao.delete(id);
	}
	protected static void normalize(UserPojo p) {
		p.setEmail(p.getEmail().toLowerCase().trim());
	}

	private UserPojo assignRole(UserPojo userPojo){
		List<String> supervisorEmailList = Arrays.asList(supervisorEmail.split("\\s*,\\s*"));
		if(supervisorEmailList.contains(userPojo.getEmail())){
			userPojo.setRole(Role.ADMIN);
		}
		return userPojo;
	}

}
