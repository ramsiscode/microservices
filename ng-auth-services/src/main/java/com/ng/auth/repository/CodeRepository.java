package com.ng.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ng.auth.model.Code;
import com.ng.auth.model.User;

@Repository
public interface CodeRepository extends JpaRepository<Code,Long> {

    List<Code> findByUser(User user);
    List<Code> findByUserUsername(String username);

}
