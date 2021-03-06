package com.microservices.auth.jwt;

import com.google.common.base.Strings;
import com.microservices.auth.repository.UserRepository;
import com.microservices.auth.tokenrepo.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class JwtTokenVerifier extends OncePerRequestFilter {

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public JwtTokenVerifier(SecretKey secretKey,
                            JwtConfig jwtConfig, UserRepository userRepository, TokenRepository tokenRepository) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("In doFilterInternal method of JwtTokenVerifier auth-server");

        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());

        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");

        try {

            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();

            String username = body.getSubject();

            List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");

            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    simpleGrantedAuthorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Long customerFk = userRepository.findByUserName(authentication.getName()).get().getCustomerFK();

            response.setHeader(org.springframework.http.HttpHeaders.COOKIE, "custFK=" + String.valueOf(customerFk));

            log.info("token valid, authorized!!");
        } catch (JwtException e) {
          //  throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
            log.info(String.format("Token %s cannot be trusted", token));
           if(tokenRepository.existsByToken(token)){
               log.info("Removing useless token");
               tokenRepository.deleteByToken(token);
           }
        }
        filterChain.doFilter(request, response);
    }
}
