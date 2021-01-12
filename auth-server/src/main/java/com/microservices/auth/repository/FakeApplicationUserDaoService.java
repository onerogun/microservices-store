package com.microservices.auth.repository;

import com.google.common.collect.Lists;
import com.microservices.auth.applicationusers.ApplicationUserDetails;
import com.microservices.auth.applicationusers.UserRoles;
import com.microservices.auth.security.PasswordConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository("fake")
public class FakeApplicationUserDaoService implements ApplicationUserDao {

    private final PasswordConfig passwordConfig;

    @Autowired
    public FakeApplicationUserDaoService(PasswordConfig passwordConfig) {
        this.passwordConfig = passwordConfig;
    }

    @Override
    public Optional<ApplicationUserDetails> selectApplicationUserByUsername(String username) {
        return getApplicationUsers()
                .stream()
                .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                .findFirst();
    }

    private List<ApplicationUserDetails> getApplicationUsers() {
        List<ApplicationUserDetails> applicationUsers = Lists.newArrayList(
                new ApplicationUserDetails(
                        UserRoles.ADMIN.getGrantedAuthority(),
                        passwordConfig.passwordEncoder().encode("password"),
                        "anna",
                        true,
                        true,
                        true,
                        true

                ),
                new ApplicationUserDetails(
                        UserRoles.USER.getGrantedAuthority(),
                        passwordConfig.passwordEncoder().encode("password"),
                        "corc",
                        true,
                        true,
                        true,
                        true

                ),
                new ApplicationUserDetails(
                        UserRoles.PRIME_USER.getGrantedAuthority(),
                        passwordConfig.passwordEncoder().encode("password"),
                        "murat",
                        true,
                        true,
                        true,
                        true

                )
        );

        return applicationUsers;
    }

}
