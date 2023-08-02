package com.increff.pos.dto;

import com.increff.pos.model.data.UserData;
import com.increff.pos.model.forms.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.CartService;
import com.increff.pos.service.UserService;
import com.increff.pos.model.enums.Role;

import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDto {
    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    public void addUser(UserForm userForm) throws ApiException {
        ValidationUtil.checkValid(userForm);
        UserPojo p = convert(userForm);
        userService.add(p);
    }

    public void deleteUser(int id) throws ApiException {
        cartService.deleteAll(userService.getId(id).getEmail());
        userService.delete(id);
    }

    public List<UserData> getAllUser() {
        List<UserPojo> list = userService.getAll();
        List<UserData> list2 = new ArrayList<UserData>();
        for (UserPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    private static UserData convert(UserPojo p) {
        UserData d = new UserData();
        d.setEmail(p.getEmail());
        d.setRole(p.getRole());
        d.setId(p.getId());
        return d;
    }

    private static UserPojo convert(UserForm f) {
        UserPojo p = new UserPojo();
        p.setEmail(f.getEmail());
        p.setRole(Role.OPERATOR);
        p.setPassword(f.getPassword());
        return p;
    }

}
