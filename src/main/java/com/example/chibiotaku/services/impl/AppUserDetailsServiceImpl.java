package com.example.chibiotaku.services.impl;

import com.example.chibiotaku.domain.entities.UserEntity;
import com.example.chibiotaku.repo.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AppUserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;


    public AppUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = this.userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with this username " + username + "dosen't exist"));

        return this.mapUser(userEntity);

    }

    private UserDetails mapUser(UserEntity userEntity){
        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getRoles()
                        .stream()
                        .map( role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().name()))
                        .collect(Collectors.toList())
        );
    }
}
