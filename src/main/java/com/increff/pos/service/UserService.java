package com.increff.pos.service;

import java.util.List;
import javax.transaction.Transactional;

import com.increff.pos.model.enums.Role;

import com.increff.pos.service.exception.ApiException;
import com.increff.pos.util.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserDao userDao;

	public void add(UserPojo p) throws ApiException {
		normalize(p);
		UserPojo existing = userDao.select(p.getEmail());
		if (existing != null) {
			throw new ApiException("User with entered email already exists!");
		}
		p.setRole(AuthenticationUtil.assignRole(p.getEmail()));
		userDao.insert(p);
	}

	public UserPojo get(String email) throws ApiException {
		return userDao.select(email);
	}

	public UserPojo getCheckId(int id) throws ApiException {
		UserPojo userPojo = userDao.selectId(id);
		if(userPojo != null)
			return userPojo;
		throw new ApiException("User with given Id does not exist !");
	}

	public List<UserPojo> getAll() {
		return userDao.selectAll();
	}

	public void delete(int id) throws ApiException {
		if(getCheckId(id).getRole() == Role.ADMIN){
			throw new ApiException("Cannot delete an Admin!");
		}
		userDao.delete(id);
	}

	protected static void normalize(UserPojo p) {
		p.setEmail(p.getEmail().toLowerCase().trim());
	}
}
