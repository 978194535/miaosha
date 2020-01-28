package com.miaoshaproject.controller;

import com.miaoshaoproject.service.Model.userModel;
import com.miaoshaoproject.service.userService;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.commonReturnType;
import com.miaoshaproject.viewObject.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController{

    @Autowired
    private userService userService1;

    @Autowired
    private HttpServletRequest httpServletRequest ;
    //用户登录接口
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public commonReturnType login(@RequestParam(name="telephone")String telephone,@RequestParam(name="password") String password ) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if(StringUtils.isEmpty(telephone)||StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"不能为空");
        }
        //用户登录服务
        boolean b = userService1.timeNo("127.0.0.1");
        if(b==false){
            throw  new BusinessException(EmBusinessError.TIME_NO);
        }
       userModel userModel1 = userService1.validateLogin(telephone,this.EncodeByMd5(password));
        //将登录凭证加入到用户登录成功的session
        this.httpServletRequest.getSession().setAttribute("login",true);
        this.httpServletRequest.getSession().setAttribute("user_login",userModel1);
        return commonReturnType.create(null);
    }
    //用户注册接口
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public commonReturnType register(@RequestParam(name = "telephone")String telephone,
                                     @RequestParam(name = "name")String name,
                                     @RequestParam(name = "gender")Integer gender,
                                     @RequestParam(name = "age")Integer age,
                                     @RequestParam(name = "password")String password) throws Exception {
        //验证手机号和对应的otpCode相符合
//        String inSessionCode = (String) this.httpServletRequest.getSession().getAttribute(telephone);
//        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionCode)){
//            throw  new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
//        }
//用户注册流程

        boolean bo = userService1.teleIsHave(telephone);
        if(bo==false) {
            throw new BusinessException(EmBusinessError.TELEPHONE_IS_EXIST);
        }
        userModel userModel1 = new userModel();
        userModel1.setName(name);
        userModel1.setGender(gender);
        userModel1.setAge(age);
        userModel1.setTelephone(telephone);
        userModel1.setRegisterMode("byphone");
        userModel1.setEncrptPassword(this.EncodeByMd5(password));

        userService1.register(userModel1);
        return commonReturnType.create(null);
    }
    private String  EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new  BASE64Encoder();
        //加密字符串
        String newStr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newStr;
    }

    //用户获取otp短信接口
//    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
////    @ResponseBody
////    public commonReturnType getOtp(@RequestParam(name = "telephone")String telephone){
////        //需要按照一定的规则生成OTP验证码
////        Random random = new Random();
////        int randomint = random.nextInt(99999);
////        randomint+=10000;
////        String otpCode = String.valueOf(randomint);
////        //将OTP验证码同时对应用户的手机号关联,使用httpsession的方式
////        httpServletRequest.getSession().setAttribute(telephone,otpCode);
////        //将OTP验证码通过短信通道发给用户,不用先
////        System.out.println("telephone:"+telephone+"otpCode"+otpCode);
////        return commonReturnType.create(null);
////    }
    @RequestMapping("/get")
    @ResponseBody
    public commonReturnType getUser(@RequestParam(name = "id")Integer id) throws BusinessException {
    //调用service服务获取对应id的用户对象并返回给前端
       userModel userModel = userService1.getUserById(id);
       //若获取的对应用户信息不存在
        if(userModel == null){
            userModel.setEncrptPassword("123");
           // throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }


        UserVo userVo = conver(userModel);
       return commonReturnType.create(userVo);

    }
    private UserVo conver(userModel userModel){

        if(userModel==null){
            return  null;
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userModel,userVo);
        return userVo;
    }

}
