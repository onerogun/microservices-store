package com.microservices.item.security.securityconfig;

import com.microservices.item.security.applicationusers.UserRoles;
import com.microservices.item.security.jwt.JwtConfig;
import com.microservices.item.security.jwt.JwtSecretKey;
import com.microservices.item.security.jwt.JwtTokenVerifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.microservices.item.security.applicationusers.UserRoles.*;

@EnableWebSecurity
@Configuration
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConfig jwtConfig;
    private final JwtSecretKey jwtSecretKey;
    private final JwtTokenVerifier jwtTokenVerifier;

    public ApplicationSecurityConfig(JwtConfig jwtConfig, JwtSecretKey jwtSecretKey, JwtTokenVerifier jwtTokenVerifier) {
        this.jwtConfig = jwtConfig;
        this.jwtSecretKey = jwtSecretKey;
        this.jwtTokenVerifier = jwtTokenVerifier;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and().requestMatchers().antMatchers("/items/deleteItem/*", "/items/addItem/*", "/items/updateItem/*", "/items/getUserItems/*")
                .and().addFilterBefore(jwtTokenVerifier, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                //.antMatchers("/items/getItems").permitAll()
                .antMatchers("/items/deleteItem/*", "/items/addItem/*", "/items/updateItem/*", "/items/getUserItems/*").hasRole(ADMIN.name())
                .anyRequest().authenticated();
    }




}
