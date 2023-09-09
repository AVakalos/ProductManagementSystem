package com.apostolis.spms.service;

import com.apostolis.spms.model.User;
import com.apostolis.spms.repository.RoleRepository;
import com.apostolis.spms.repository.UserRepository;


import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.apostolis.spms.model.RegisterDTO;
import com.apostolis.spms.model.Role;

/*
Custom implementation for:
    1. Loading user information from the database.
    2. Storing a new user to database.
*/

@Service
public class AppUserDetailsServiceImpl implements AppUserDetailsService {
    
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AppUserDetailsServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: "+ usernameOrEmail));

        Set<GrantedAuthority> authorities = user
            .getRoles()
            .stream()
            .map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
    
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
            user.getPassword(),
            authorities);
    }

    @Override
    public User save(RegisterDTO registeredUserDTO){
		User user = new User();
		user.setUsername(registeredUserDTO.getUsername());
        user.setEmail(registeredUserDTO.getEmail());
		user.setPassword(passwordEncoder.encode(registeredUserDTO.getPassword()));
        Role roles = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singleton(roles));
		
		return userRepository.save(user);
    }
}
