package com.increff.pos.service;

import com.increff.pos.dto.LoginDto;
import com.increff.pos.dto.UserDto;
import com.increff.pos.model.forms.LoginForm;
import com.increff.pos.model.forms.UserForm;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;

public class LoginDtoTest extends AbstractUnitTest{

    @Autowired
    LoginDto loginDto;

    @Autowired
    UserDto userDto;

    @Before
    public void addTestData() throws ApiException {
        UserForm userForm = new UserForm();
        userForm.setEmail("test@test.com");
        userForm.setPassword("1234");
        userDto.addUser(userForm);
    }

    @Test
    public void testLogin() throws ApiException{
        LoginForm loginForm = new LoginForm();
        loginForm.setEmail("test@test.com");
        loginForm.setPassword("1234");
        HttpServletRequest request = new MockHttpServletRequest();
        ModelAndView mav = new ModelAndView("redirect:/ui/home");
        assertEquals(mav.getViewName(), loginDto.login(request,loginForm).getViewName());
    }

    @Test
    public void testInvalidLogin() throws ApiException {
        LoginForm loginForm = new LoginForm();
        loginForm.setEmail("invalid@test.com");
        loginForm.setPassword("1234");
        HttpServletRequest request = new MockHttpServletRequest();
        ModelAndView mav = new ModelAndView("redirect:/site/login");
        assertEquals(mav.getViewName(), loginDto.login(request,loginForm).getViewName());
    }

    @Test
    public void testLogout() throws ApiException{
        LoginForm loginForm = new LoginForm();
        loginForm.setEmail("test@test.com");
        loginForm.setPassword("1234");
        HttpServletRequest request = new MockHttpServletRequest();
        loginDto.login(request,loginForm);
        HttpServletResponse response = new MockHttpServletResponse();
        ModelAndView mav = new ModelAndView("redirect:/");
        assertEquals(mav.getViewName(), loginDto.logout(request, response).getViewName());
    }
}
