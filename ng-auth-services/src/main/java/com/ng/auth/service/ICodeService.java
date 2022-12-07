package com.ng.auth.service;

import java.util.List;

import com.ng.auth.model.Code;
import com.ng.auth.model.User;

public interface ICodeService {

    void saveCode(List<Code> code);

    List<Code> getSecurityCode(User user);
    public List<Code> getScurityCodeOfUser(String username);


}
