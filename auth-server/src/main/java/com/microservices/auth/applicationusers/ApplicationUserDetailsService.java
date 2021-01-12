package com.microservices.auth.applicationusers;

import com.microservices.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public ApplicationUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

/*
    private final ApplicationUserDao applicationUserDao;;

    @Autowired
    public ApplicationUserDetailsService(@Qualifier("fake")ApplicationUserDao applicationUserDao) {
        this.applicationUserDao = applicationUserDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return applicationUserDao
                .selectApplicationUserByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("Username %s not found", username))
                );
    }
*/

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(s).orElseThrow(() -> new UsernameNotFoundException("User " + s + " does not exist!"));
        UserDetails userDetails = new ApplicationUserDetails(user.getRoles().getGrantedAuthority(),
                user.getPassword(),user.getUserName(),
                user.isAccountNonExpired(), user.isAccountNonLocked(),
                user.isCredentialsNonExpired(), user.isEnabled() );
        return userDetails;
    }
}
