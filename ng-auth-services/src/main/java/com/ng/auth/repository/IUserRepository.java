package com.ng.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ng.auth.model.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
	User findByUsername(String userName);
}
