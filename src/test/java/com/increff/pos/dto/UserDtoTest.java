package com.increff.pos.dto;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.UserDao;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.enums.Role;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.UserService;
import com.increff.pos.service.exception.ApiException;
import com.increff.pos.util.TestUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;


public class UserDtoTest extends AbstractUnitTest {

    @Autowired
    private UserDto userDto;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;

    //Test add new user
    @Test
    public void testAddUser() throws ApiException {
        UserForm userForm = TestUtil.getUserForm("test@test.com","1234");
        userDto.addUser(userForm);
        UserPojo userPojo = userDao.selectAll().get(0);
        assertEquals("test@test.com",userPojo.getEmail());
        assertEquals("1234",userPojo.getPassword());
    }

    //Test delete a user
    @Test
    public void testDeleteUser() throws ApiException {
        UserForm userForm = TestUtil.getUserForm("test@test.com","1234");
        userDto.addUser(userForm);
        int userId = userDao.selectAll().get(0).getId();
        userDto.deleteUser(userId);
        try{
            userService.getCheckId(userId);
            fail();
        }catch (Exception e){
            assertEquals("User with given Id does not exist !",e.getMessage());
        }
    }

    //Test get all users
    @Test
    public void testGetAll() throws ApiException {
        List<UserForm> userFormList = TestUtil.getUserFormList();
        for(int i = 0; i<userFormList.size(); i++){
            userDto.addUser(userFormList.get(i));
        }

        List<UserData> userDataList = userDto.getAllUser();
        for(int i=0; i<userDataList.size(); i++){
            assertEquals("test"+i+"@test.com", userDataList.get(i).getEmail());
            assertEquals(Role.OPERATOR, userDataList.get(i).getRole());
        }
    }
}
