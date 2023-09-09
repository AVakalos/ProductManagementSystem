package com.apostolis.spms.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.apostolis.spms.model.User;
import com.apostolis.spms.model.RegisterDTO;

public interface AppUserDetailsService extends UserDetailsService{
    User save(RegisterDTO userRegisteredDTO);
}
