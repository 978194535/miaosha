package com.miaoshaproject;

import com.miaoshaproject.dao.UserDaoMapper;
import com.miaoshaproject.dataobjcet.UserDao;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = {"com"})
@RestController
@MapperScan("com.miaoshaproject.dao")
public class App
{
    @Autowired
    private UserDaoMapper userDaoMapper;

    @RequestMapping("/")
    public String home(){
       UserDao userDao =  userDaoMapper.selectByPrimaryKey(1);
       if(userDao == null){
           return "用户不存在";
       }else{
           return userDao.getName();
       }

    }
    public static void main( String[] args )
    {

        SpringApplication.run(App.class,args);
    }
}
