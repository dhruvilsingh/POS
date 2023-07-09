package com.increff.pos.dto;

import com.increff.pos.model.InfoData;
import com.increff.pos.model.LoginForm;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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

//    public ModelAndView signUp(HttpServletRequest request, LoginForm loginForm) throws ApiException {
//        UserPojo userPojo = userService.get(loginForm.getEmail());
//        if(userPojo != null){
//            infoData.setMessage("Email already registered! Try logging in");
//        }
//        userPojo = convert(loginForm);
//        userService.add(userPojo);
//        infoData.setEmail(userPojo.getEmail());
//        return authenticateUser(request, userPojo);
//    }

    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return new ModelAndView("redirect:/site/logout");
    }

    private static ModelAndView authenticateUser(HttpServletRequest request , UserPojo userPojo){
        // Create authentication object
        Authentication authentication = convert(userPojo);
        // Create new session
        HttpSession session = request.getSession(true);
        // Attach Spring SecurityContext to this new session
        SecurityUtil.createContext(session);
        // Attach Authentication object to the Security Context
        SecurityUtil.setAuthentication(authentication);
        return new ModelAndView("redirect:/ui/home");
    }

    private static UserPojo convert(LoginForm loginForm){
        UserPojo userPojo = new UserPojo();
        userPojo.setEmail(loginForm.getEmail());
        userPojo.setPassword(loginForm.getPassword());
        userPojo.setRole("supervisor");
        return userPojo;
    }

    private static Authentication convert(UserPojo userPojo) {
        // Create principal
        UserPrincipal principal = new UserPrincipal();
        principal.setEmail(userPojo.getEmail());
        principal.setId(userPojo.getId());
        // Create Authorities
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(userPojo.getRole()));
        // you can add more roles if required
        // Create Authentication
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null,
                authorities);
        return token;
    }
}
