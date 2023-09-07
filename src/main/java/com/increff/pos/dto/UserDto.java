package com.increff.pos.dto;

import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.exception.ApiException;
import com.increff.pos.service.CartService;
import com.increff.pos.service.UserService;

import com.increff.pos.util.ConversionUtil;
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
        UserPojo p = ConversionUtil.convert(userForm, UserPojo.class);
        userService.add(p);
    }

    public void deleteUser(int id) throws ApiException {
        cartService.deleteAll(userService.getCheckId(id).getEmail());
        userService.delete(id);
    }

    public List<UserData> getAllUser() throws ApiException {
        List<UserPojo> list = userService.getAll();
        List<UserData> list2 = new ArrayList<UserData>();
        for (UserPojo p : list) {
            list2.add(ConversionUtil.convert(p, UserData.class));
        }
        return list2;
    }

}
