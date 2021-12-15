package com.comodo.todolistspring.service;

import com.comodo.todolistspring.repository.UserRepository;
import com.comodo.todolistspring.utils.AppUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public AppUserPrincipal loadUserByUsername(String s) throws UsernameNotFoundException {
        var userOptional = userRepository.findUserByUsername(s);
        if (userOptional.isEmpty()) throw new UsernameNotFoundException(String.format("The username %s doesn't exist", s));
        var user = userOptional.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new AppUserPrincipal(user, authorities);
    }

}
