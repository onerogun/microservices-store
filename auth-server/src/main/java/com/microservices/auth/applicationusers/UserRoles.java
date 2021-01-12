package com.microservices.auth.applicationusers;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum UserRoles {
    ADMIN,
    ADMIN_TRAINEE,
    USER,
    PRIME_USER;

    public SimpleGrantedAuthority getGrantedAuthority(){
        return new SimpleGrantedAuthority("ROLE_" + this.name());
    }

}
