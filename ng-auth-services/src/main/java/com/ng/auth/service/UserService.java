package com.ng.auth.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ng.auth.model.User;
import com.ng.auth.repository.IUserRepository;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	private IUserRepository iUserRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		System.out.println(userName);
		 User user = iUserRepository.findByUsername(userName);
		 System.out.println(user+"******");
		 return new org.springframework.security.core.userdetails.User(userName, user.getPasswordHash(),new ArrayList<GrantedAuthority>());
	}

//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return NoOpPasswordEncoder.getInstance();
//	}
}
