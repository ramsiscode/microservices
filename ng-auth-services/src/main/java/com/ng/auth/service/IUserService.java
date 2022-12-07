package com.ng.auth.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ng.auth.model.User;

public interface IUserService {

    User saveUser(User user);

    Optional<User> getUserByUserName(String username);

    boolean getAdditionalSecurity(Long userId);

    void setAdditionalSecurity(Long userId);

    void updateAdditionalSecurity(Long userId);

	UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException;
}
