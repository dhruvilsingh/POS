package com.increff.pos.spring;

import com.increff.pos.model.enums.Role;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static Logger logger = Logger.getLogger(SecurityConfig.class);

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http//
			// Match only these URLs
				.requestMatchers()//
				.antMatchers("/api/**")//
				.antMatchers("/ui/**")//
				.and().authorizeRequests()//
				.antMatchers(HttpMethod.POST,"/api/brands").hasAuthority(Role.ADMIN.toString().toString())//
				.antMatchers("/api/brands/upload").hasAuthority(Role.ADMIN.toString())//
				.antMatchers("/api/brands/{id}").hasAuthority(Role.ADMIN.toString())//
				.antMatchers(HttpMethod.POST,"/api/products").hasAuthority(Role.ADMIN.toString())//
				.antMatchers("/api/products/upload").hasAuthority(Role.ADMIN.toString())//
				.antMatchers("/api/products/{id}").hasAuthority(Role.ADMIN.toString())//
				.antMatchers(HttpMethod.PUT,"/api/inventory").hasAuthority(Role.ADMIN.toString())//
				.antMatchers("/api/inventory/upload").hasAuthority(Role.ADMIN.toString())//
				.antMatchers("/api/inventory/{id}").hasAuthority(Role.ADMIN.toString())//
				.antMatchers("/api/reports/daily-sales").hasAuthority(Role.ADMIN.toString())//
				.antMatchers("/api/reports/inventory").hasAuthority(Role.ADMIN.toString())//
				.antMatchers("/api/users").hasAuthority(Role.ADMIN.toString())//
				.antMatchers("/api/users/{id}").hasAuthority(Role.ADMIN.toString())//
				.antMatchers("/api/**").hasAnyAuthority(Role.ADMIN.toString(), Role.OPERATOR.toString())//
				.antMatchers("/ui/dailysales").hasAuthority(Role.ADMIN.toString())//
				.antMatchers("/ui/brandreport").hasAuthority(Role.ADMIN.toString())//
				.antMatchers("/ui/inventoryreport").hasAuthority(Role.ADMIN.toString())//
				.antMatchers("/ui/salereport").hasAuthority(Role.ADMIN.toString())//
				.antMatchers("/ui/user").hasAuthority(Role.ADMIN.toString())//
				.antMatchers("/ui/**").hasAnyAuthority(Role.ADMIN.toString(), Role.OPERATOR.toString())//
				// Ignore CSRF and CORS
				.and().csrf().disable().cors().disable();
		logger.info("Configuration complete");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
				"/swagger-ui.html", "/webjars/**");
	}

}
