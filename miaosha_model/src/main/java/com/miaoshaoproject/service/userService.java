package com.miaoshaoproject.service;

import com.miaoshaoproject.service.Model.userModel;
import com.miaoshaproject.error.BusinessException;


public interface userService {

    userModel getUserById(Integer id);
    void register(userModel userModel1) throws Exception;
    userModel validateLogin(String telephone, String encrptPassword) throws BusinessException;
    boolean teleIsHave(String telephone)throws Exception;
    boolean timeNo(String ip);

}
