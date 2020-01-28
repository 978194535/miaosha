package com.miaoshaoproject.service.impl;

import com.miaoshaoproject.service.Model.userModel;
import com.miaoshaoproject.service.userService;
import com.miaoshaproject.dao.UserDaoMapper;
import com.miaoshaproject.dao.UserPasswordDaoMapper;
import com.miaoshaproject.dataobjcet.UserDao;
import com.miaoshaproject.dataobjcet.UserPasswordDao;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.*;

@Service
public class userServiceImpl implements userService {

    @Autowired
    private UserDaoMapper userDaoMapper;

    @Autowired
    private UserPasswordDaoMapper userPasswordDaoMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override

    public userModel getUserById(Integer id) {

        UserDao userDao = userDaoMapper.selectByPrimaryKey(id);
        if(userDao==null){
            return  null;
        }
        //通过用户ID获取对应的用户加密密码信息
       UserPasswordDao userPasswordDao =  userPasswordDaoMapper.selectByUserId(userDao.getId());

        return convertFromDataObject(userDao,userPasswordDao);
    }


    @Override
    @Transactional
    public void register(userModel userModel1) throws Exception{
        if(userModel1==null){
            throw  new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"userModel1为空");
        }
        if(StringUtils.isEmpty(userModel1.getName())
                || userModel1.getGender()==null
                || userModel1.getAge()==null
                ||StringUtils.isEmpty(userModel1.getTelephone())){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"有数据为空");
        }
        UserDao userDao = conver(userModel1);
        try{
            userDaoMapper.insertSelective(userDao);
        }catch (DuplicateKeyException ex){
           throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号有了");
    }

        userModel1.setId(userDao.getId());

        UserPasswordDao userPasswordDao = convertPassword(userModel1);
        try{
            userPasswordDaoMapper.insertSelective(userPasswordDao);
        }catch (Exception ex){
            ex.printStackTrace();
        }


        return;
    }

    @Override
    public userModel validateLogin(String telephone, String encrptPassword) throws BusinessException {
        //取出对应的用户信息
        ExecutorService pools= Executors.newFixedThreadPool(3);
        Callable<UserDao> aa = new Callable<UserDao>() {

            @Override
            public UserDao call() throws Exception {
                UserDao userDao =  userDaoMapper.selectByTelephone(telephone);
                return userDao;
            }
        };
        Future<UserDao> tassk1=pools.submit(aa);

        UserDao userDao = null;
        try {
            userDao = tassk1.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(userDao == null){
           throw new BusinessException(EmBusinessError.LOGIN_FAIL);
       }
        int id = userDao.getId();
        Callable<UserPasswordDao> bb = new Callable<UserPasswordDao>() {

            @Override
            public UserPasswordDao call() throws Exception {
                UserPasswordDao userPasswordDao = userPasswordDaoMapper.selectByPrimaryKey(id);
                return userPasswordDao;
            }
        };
        Future<UserPasswordDao> tassk2=pools.submit(bb);

        UserPasswordDao userPasswordDao = null;
        try {
            userPasswordDao = tassk2.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        userModel  userModel1 = convertFromDataObject(userDao,userPasswordDao);
       //校验密码是否一致
        if(!StringUtils.equals(encrptPassword,userModel1.getEncrptPassword())){
            throw new BusinessException((EmBusinessError.LOGIN_FAIL));
        }
        return userModel1;

    }

    @Override
    public boolean teleIsHave(String telephone) throws Exception {
        UserDao userDao = userDaoMapper.selectByTelephone(telephone);
        if(userDao==null) return true;
        return false;
    }

    @Override
    public boolean timeNo(String ip) {
        String value = stringRedisTemplate.opsForValue().get(ip);

        if(value==null){
            stringRedisTemplate.opsForValue().set(ip,"1");
        }else {
            stringRedisTemplate.opsForValue().increment(ip);
            int a = Integer.parseInt(value);
            if (a>2) {
                return false;
            }
        }
        return true;
    }

    private UserPasswordDao convertPassword(userModel userModel){
        if(userModel==null){
            return null;
        }
        UserPasswordDao userPasswordDao = new UserPasswordDao();
        userPasswordDao.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDao.setId(userModel.getId());
        return userPasswordDao;
    }
    private UserDao conver(userModel userModel){
        if(userModel==null){
            return null;
        }
        UserDao userDao = new UserDao();
        BeanUtils.copyProperties(userModel,userDao);
        return userDao;
    }


    private userModel convertFromDataObject(UserDao userDao, UserPasswordDao userPasswordDao){
        if(userDao==null){
            return  null;
        }
        userModel userModel = new userModel();
        BeanUtils.copyProperties(userDao,userModel);
        if(userPasswordDao!=null){
            userModel.setEncrptPassword(userPasswordDao.getEncrptPassword());
        }
        return userModel;
    }
}
