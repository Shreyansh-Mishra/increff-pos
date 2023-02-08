package com.increff.pos.spring;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@EnableScheduling
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
				.antMatchers(HttpMethod.GET,"/api/brand/**").hasAnyAuthority("supervisor", "operator")//
				.antMatchers(HttpMethod.POST, "/api/brand/**").hasAnyAuthority("supervisor")//
				.antMatchers(HttpMethod.PUT, "/api/brand/**").hasAnyAuthority("supervisor")//
				.antMatchers(HttpMethod.DELETE, "/api/brand/**").hasAnyAuthority("supervisor")//
				.antMatchers(HttpMethod.GET,"/api/product/**").hasAnyAuthority("supervisor","operator")//
				.antMatchers(HttpMethod.POST, "/api/product/**").hasAnyAuthority("supervisor")//
				.antMatchers(HttpMethod.PUT, "/api/product/**").hasAnyAuthority("supervisor")//
				.antMatchers(HttpMethod.DELETE, "/api/product/**").hasAnyAuthority("supervisor")//
				.antMatchers(HttpMethod.GET, "/api/inventory/**").hasAnyAuthority("supervisor","operator")//
				.antMatchers(HttpMethod.POST, "/api/inventory/**").hasAnyAuthority("supervisor")//
				.antMatchers(HttpMethod.PUT, "/api/inventory/**").hasAnyAuthority("supervisor")//
				.antMatchers(HttpMethod.DELETE, "/api/inventory/**").hasAnyAuthority("supervisor")//
				.antMatchers(HttpMethod.GET, "/api/order/**").hasAnyAuthority("supervisor","operator")//
				.antMatchers(HttpMethod.POST, "/api/order/**").hasAnyAuthority("supervisor","operator")//
				.antMatchers(HttpMethod.PUT, "/api/order/**").hasAnyAuthority("supervisor")//
				.antMatchers(HttpMethod.DELETE, "/api/order/**").hasAnyAuthority("supervisor")//
				.antMatchers("/api/report/**").hasAnyAuthority("supervisor")//
				.antMatchers("/api/**").hasAnyAuthority("supervisor", "operator")//
				.antMatchers("/ui/report/**").hasAnyAuthority("supervisor")//
				.antMatchers("/ui/**").hasAnyAuthority("supervisor", "operator")//
				.and().exceptionHandling().accessDeniedHandler(accessDeniedHandler())//
				//handle accessDenied when not logged in
				.and().exceptionHandling().accessDeniedPage("/site/403")//
				.and().csrf().disable().cors().disable();

		logger.info("Configuration complete");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
				"/swagger-ui.html", "/webjars/**");
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

}
