package com.increff.pos.dto;

import com.increff.pos.model.data.InfoData;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Component
public class AbstractUiDto {
    @Autowired
    private InfoData info;
    @Value("${app.baseUrl}")
    private String baseUrl;

    protected ModelAndView mav(String page) {
        // Get current user
        UserPrincipal principal = SecurityUtil.getPrincipal();
        info.setEmail(principal == null ? "" : principal.getEmail());
        info.setRole(principal == null ? "" : principal.getRole());
        // Set info
        ModelAndView mav = new ModelAndView(page);
        mav.addObject("info", info);
        mav.addObject("role", info.getRole());
        mav.addObject("baseUrl", baseUrl);
        return mav;
    }
}
