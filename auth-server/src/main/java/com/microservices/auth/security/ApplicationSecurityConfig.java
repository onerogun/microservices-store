package com.microservices.auth.security;


import com.microservices.auth.applicationusers.ApplicationUserDetailsService;
import com.microservices.auth.jwt.JwtConfig;
import com.microservices.auth.jwt.JwtTokenVerifier;
import com.microservices.auth.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.microservices.auth.repository.UserRepository;
import com.microservices.auth.streamchannel.OutputChannel;
import com.microservices.auth.tokenrepo.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

import static com.microservices.auth.applicationusers.UserRoles.*;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableBinding(OutputChannel.class)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserDetailsService applicationUserDetailsService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final OutputChannel outputChannel;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder,
                                     ApplicationUserDetailsService applicationUserService,
                                     SecretKey secretKey,
                                     JwtConfig jwtConfig, TokenRepository tokenRepository, UserRepository userRepository, OutputChannel outputChannel) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserDetailsService = applicationUserService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.outputChannel = outputChannel;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    .cors().and()
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().antMatchers("/auth/passwordReset/checkLinkValidity/*","/auth/resetPassword","/auth/resetPasswordByLink/*", "/auth/save", "/auth/saveCustomer").permitAll().and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey,tokenRepository, userRepository, outputChannel))
                .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig, userRepository),JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
              //  .antMatchers("/auth/save", "/auth/saveCustomer").permitAll()
                .antMatchers("/auth/delete/*", "/auth/update/*", "/auth/getAll", "/auth/getUser/*").hasAnyRole(ADMIN.name(), USER.name(), PRIME_USER.name())
                .anyRequest().authenticated();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserDetailsService);
        return provider;
    }
/*
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/login", configuration);
        return source;
    }
*/
}
