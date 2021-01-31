package com.microservices.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import com.microservices.auth.repository.UserRepository;
import com.microservices.auth.streamchannel.OutputChannel;
import com.microservices.auth.tokenrepo.TokenInDB;
import com.microservices.auth.tokenrepo.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

@Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final OutputChannel outputChannel;





    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                      JwtConfig jwtConfig,
                                                      SecretKey secretKey, TokenRepository tokenRepository, UserRepository userRepository, OutputChannel outputChannel) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.outputChannel = outputChannel;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        log.info("In attemptAuthentication method of JwtUsernameAndPasswordAuthenticationFilter");
        try {
            UsernameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);


            Authentication authentication = new UsernamePasswordAuthenticationToken(

                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            );


            Authentication authenticate = authenticationManager.authenticate(authentication);
            return authenticate;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest httpServletRequest,
                                            HttpServletResponse httpServletResponse,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        log.info("In successfulAuthentication method of JwtUsernameAndPasswordAuthenticationFilter");
        String token;

        if(!tokenRepository.existsById(authentication.getName())) {
            log.info("Token does not exist in DB, creating new one and inserting into DB");
             token = Jwts.builder()
                    .setSubject(authentication.getName())
                    .claim("authorities", authentication.getAuthorities())
                    .setIssuedAt(new Date())
                    .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                    .signWith(secretKey)
                    .compact();
            tokenRepository.save(new TokenInDB(authentication.getName(), token));
        }else {
            log.info("Token exists in DB and returning it from db");
           token = tokenRepository.findById(authentication.getName()).get().getToken();

        }


        String bearerToken = jwtConfig.getTokenPrefix() + token;
        //httpServletResponse.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
         //       HttpHeaders.AUTHORIZATION + "," + HttpHeaders.COOKIE + "," + HttpHeaders.SET_COOKIE);
        httpServletResponse.addHeader(HttpHeaders.AUTHORIZATION, bearerToken);

        Long customerFk = userRepository.findByUserName(authentication.getName()).get().getCustomerFK();
    /*    Cookie cookie = new Cookie("customerFk", String.valueOf(customerFk));
       cookie.setMaxAge((int) Duration.ofDays(14).getSeconds());
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
*/
      //  httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.SET_COOKIE);
      //  httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,HttpHeaders.COOKIE);
       // httpServletResponse.setHeader(HttpHeaders.SET_COOKIE2, HttpHeaders.COOKIE + ":" + "custFK=" + String.valueOf(customerFk));
        httpServletResponse.setHeader(org.springframework.http.HttpHeaders.COOKIE, "custFK=" + String.valueOf(customerFk));

        log.info("Sending new sign-in alert email");
        outputChannel.signInAlert().send(MessageBuilder.withPayload(customerFk).build());
    }
}
