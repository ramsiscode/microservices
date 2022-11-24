package com.ng.auth.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ng.auth.exc_handler.UserNameFoundException;
import com.ng.auth.model.User;
import com.ng.auth.repository.UserRepository;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        if(getUserByUserName(user.getUsername()).isPresent()){
            throw new UserNameFoundException(user.getUsername() + " already exist!!!");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByUserName(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user;
    }

    @Override
    public boolean getAdditionalSecurity(Long userId) {
        User user =userRepository.findById(userId).get();
        return user.getAdditionalSecurity();
    }

    @Override
    public void setAdditionalSecurity(Long userId){
        User user =userRepository.findById(userId).get();
        user.setAdditionalSecurity(true);
    }

    @Override
    public void updateAdditionalSecurity(Long userId){
        User user =userRepository.findById(userId).get();
        user.setAdditionalSecurity(false);
    }
    @Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		System.out.println(userName);
		 User user = userRepository.findByUsername(userName).get();
		 System.out.println(user+"******");
		 return new org.springframework.security.core.userdetails.User(userName, user.getPasswordHash(),new ArrayList<GrantedAuthority>());
	}


}
