package com.ng.auth.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ng.auth.model.Code;
import com.ng.auth.model.User;
import com.ng.auth.repository.CodeRepository;

@Service
@Transactional
public class CodeServiceImpl implements ICodeService {

    @Autowired
    private CodeRepository codeRepository;

    //get backup codes generated
    @Override
    public void saveCode(List<Code> code) {
        codeRepository.saveAll(code);
    }

    @Override
    public List<Code> getSecurityCode(User user) {
        return codeRepository.findByUser(user);
    }
    @Override
    public List<Code> getScurityCodeOfUser(String username){
    	return codeRepository.findByUserUsername(username);
    }


}
