package com.increff.pos.util;

import com.increff.pos.model.enums.Role;
import com.increff.pos.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthenticationUtil {

    @Value("${supervisor.email}")
    private static String supervisorEmail;

    public static Role assignRole(String email){
        if(supervisorEmail == null)
            return Role.OPERATOR;
        List<String> supervisorEmailList = Arrays.asList(supervisorEmail.split("\\s*,\\s*"));
        if(!supervisorEmailList.contains(email))
            return Role.OPERATOR;
        return Role.ADMIN;
    }

    public static Authentication convert(UserPojo userPojo) {
        // Create principal
        UserPrincipal principal = new UserPrincipal();
        principal.setEmail(userPojo.getEmail());
        principal.setId(userPojo.getId());
        principal.setRole(userPojo.getRole().toString());
        // Create Authorities
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(userPojo.getRole().toString()));
        // you can add more roles if required
        // Create Authentication
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null,
                authorities);
        return token;
    }
}
