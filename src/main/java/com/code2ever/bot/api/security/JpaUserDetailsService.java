package com.code2ever.bot.api.security;


import com.code2ever.bot.data.entity.UserApp;
import com.code2ever.bot.data.service.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Supplier<UsernameNotFoundException> supplier = () -> new UsernameNotFoundException("Problem during authentication!");
        UserApp userApp = userRepository.getUserByUserName(username).orElseThrow(supplier);
        return new CustomUserDetails(userApp);
    }
}
