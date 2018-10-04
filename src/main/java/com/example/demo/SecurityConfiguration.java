package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("user").password("{noop}pass").roles("USER");
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
                .antMatchers("/login.jspx").permitAll()
				.antMatchers("/**").authenticated()
				.and()
				.formLogin()
				.loginProcessingUrl("/authenticate").usernameParameter("login").passwordParameter("passwd")
                .loginPage("/login.jspx")
                .failureUrl("/login.jspx?error=true")
                .defaultSuccessUrl("/")
				.and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.jspx")
                .invalidateHttpSession(true)
                .and().csrf().disable()
                .headers().frameOptions().sameOrigin();
	}




}
