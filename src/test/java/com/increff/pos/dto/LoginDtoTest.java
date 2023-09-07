package com.increff.pos.dto;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.model.data.InfoData;
import com.increff.pos.model.form.LoginForm;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.service.exception.ApiException;
import com.increff.pos.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;

public class LoginDtoTest extends AbstractUnitTest {

    @Autowired
    private LoginDto loginDto;
    @Autowired
    private UserDto userDto;
    @Autowired
    private InfoData infoData;
    private final String userEmail = "test@test.com";


    @Before
    public void addTestData() throws ApiException {
        UserForm userForm = TestUtil.getUserForm(userEmail,"1234");
        userDto.addUser(userForm);
    }

    //Test login
    @Test
    public void testLogin() throws ApiException{
        LoginForm loginForm = TestUtil.getLoginForm(userEmail,"1234");
        HttpServletRequest request = new MockHttpServletRequest();
        ModelAndView mav = new ModelAndView("redirect:/ui/home");
        assertEquals(mav.getViewName(), loginDto.login(request,loginForm).getViewName());
        assertEquals(infoData.getEmail(),userEmail);
    }

    //Test invalid user login
    @Test
    public void testInvalidLogin() throws ApiException {
        LoginForm loginForm = TestUtil.getLoginForm("invalidEmail@test.com", "1234");
        HttpServletRequest request = new MockHttpServletRequest();
        ModelAndView mav = new ModelAndView("redirect:/site/login");
        assertEquals(mav.getViewName(), loginDto.login(request,loginForm).getViewName());
        assertEquals("Invalid username or password",infoData.getMessage());
    }

    //Test sign up
    @Test
    public void testSignUp() throws ApiException {
        LoginForm loginForm = TestUtil.getLoginForm("test1@test.com", "1234");
        HttpServletRequest request = new MockHttpServletRequest();
        ModelAndView mav = new ModelAndView("redirect:/ui/home");
        assertEquals(mav.getViewName(), loginDto.signUp(request,loginForm).getViewName());
        assertEquals("test1@test.com",infoData.getEmail());
    }

    //Test signing up existing user
    @Test
    public void testExistingSignUp() throws ApiException {
        LoginForm loginForm = TestUtil.getLoginForm(userEmail, "1234");
        HttpServletRequest request = new MockHttpServletRequest();
        ModelAndView mav = new ModelAndView("redirect:/site/signup");
        assertEquals(mav.getViewName(), loginDto.signUp(request,loginForm).getViewName());
        assertEquals("Email already registered! Try logging in",infoData.getMessage());
    }

    //Test logout
    @Test
    public void testLogout() throws ApiException{
        LoginForm loginForm = TestUtil.getLoginForm(userEmail,"1234");
        HttpServletRequest request = new MockHttpServletRequest();
        loginDto.login(request,loginForm);
        HttpServletResponse response = new MockHttpServletResponse();
        ModelAndView mav = new ModelAndView("redirect:/");
        assertEquals(mav.getViewName(), loginDto.logout(request, response).getViewName());
    }
}
