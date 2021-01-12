package com.microservices.item.security.applicationusers;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum UserRoles {
    ADMIN(Sets.newHashSet(UserPermissions.USER_UPDATE, UserPermissions.ITEM_UPDATE)),
    USER(Sets.newHashSet()),
    PRIME_USER(Sets.newHashSet());

    private final Set<UserPermissions> permissions;


    UserRoles(Set<UserPermissions> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermissions> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = this.permissions
                .stream().map(userPermissions -> new SimpleGrantedAuthority(userPermissions.getPermission())).collect(Collectors.toSet());
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return simpleGrantedAuthorities;
    }

}
