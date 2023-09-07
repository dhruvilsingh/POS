package com.increff.pos.dto;

import com.increff.pos.model.data.InfoData;
import com.increff.pos.model.form.LoginForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.exception.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.util.AuthenticationUtil;
import com.increff.pos.util.ConversionUtil;
import com.increff.pos.util.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Component
public class LoginDto {

    @Autowired
    private UserService userService;
    @Autowired
    private InfoData infoData;


    public ModelAndView login(HttpServletRequest request, LoginForm loginForm) throws ApiException {
        UserPojo userPojo = userService.get(loginForm.getEmail());
        boolean authenticated = (userPojo != null && Objects.equals(userPojo.getPassword(), loginForm.getPassword()));
        if (!authenticated) {
            infoData.setMessage("Invalid username or password");
            return new ModelAndView("redirect:/site/login");
        }
        infoData.setEmail(userPojo.getEmail());
        return authenticateUser(request,userPojo);
    }

    public ModelAndView signUp(HttpServletRequest request, LoginForm loginForm) throws ApiException {
        UserPojo userPojo = userService.get(loginForm.getEmail());
        if(userPojo != null){
            infoData.setMessage("Email already registered! Try logging in");
            return new ModelAndView("redirect:/site/signup");
        }
        userPojo = ConversionUtil.convert(loginForm, UserPojo.class);
        userService.add(userPojo);
        infoData.setEmail(userPojo.getEmail());
        return authenticateUser(request, userPojo);
    }

    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return new ModelAndView("redirect:/");
    }

    private static ModelAndView authenticateUser(HttpServletRequest request , UserPojo userPojo){
        // Create authentication object
        Authentication authentication = AuthenticationUtil.convert(userPojo);
        // Create new session
        HttpSession session = request.getSession(true);
        // Attach Spring SecurityContext to this new session
        SecurityUtil.createContext(session);
        // Attach Authentication object to the Security Context
        SecurityUtil.setAuthentication(authentication);
        return new ModelAndView("redirect:/ui/home");
    }

}
