package com.miaoshaproject.controller;

import com.miaoshaoproject.service.Model.userModel;
import com.miaoshaoproject.service.Model.userModell;
import com.miaoshaoproject.service.userService;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.commonReturnType;
import com.miaoshaproject.socket.HttpApi;
import com.miaoshaproject.viewObject.UserVo;
import net.sf.json.JSONObject;
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
    public commonReturnType login(@RequestParam(name="username")String username,@RequestParam(name="password") String password ) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"不能为空");
        }
//        JSONObject jsonObject = HttpApi.getInstance().LOGIN(username,password);
//        if(jsonObject.equals(200)) {
            if(1==1) {
                userModell u1 = new userModell();
                u1.setPasswoed(password);
                u1.setUsername(username);
                //将登录凭证加入到用户登录成功的session
                this.httpServletRequest.getSession().setAttribute("login",true);
                this.httpServletRequest.getSession().setAttribute("user_login",u1);
                return commonReturnType.create(null);
            }else {
                throw new BusinessException(EmBusinessError.ERRORPASSWOED,"账号密码错误");
            }

              //用户登录服务
//        boolean b = userService1.timeNo("127.0.0.1");
//        if(b==false){
//            throw  new BusinessException(EmBusinessError.TIME_NO);
//        }
    }

    //用户名获取接口
    @RequestMapping(value = "/getname",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public commonReturnType getname(@RequestParam(name="a")String a) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        //将登录凭证加入到用户登录成功的session
        userModell usermodel = (userModell) this.httpServletRequest.getSession().getAttribute("user_login");
        if(usermodel != null){
            return commonReturnType.create(usermodel);
        }else {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还没有登录");
        }

    }

    //用户注册接口
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public commonReturnType register(@RequestParam(name = "username")String username,
                                     @RequestParam(name = "password")String password,
                                     @RequestParam(name = "description")String description,
                                     @RequestParam(name = "groupid")String groupid
                                     ) throws Exception {
        //验证手机号和对应的otpCode相符合
//        String inSessionCode = (String) this.httpServletRequest.getSession().getAttribute(telephone);
//        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionCode)){
//            throw  new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
//        }
//用户注册流程
        JSONObject jsonObject = HttpApi.getInstance().POSTNEWUSER(username,password,description,groupid);
        if(jsonObject.equals(200)) {
            return commonReturnType.create(null);
        }else {
            throw new BusinessException(EmBusinessError.REGISTER_ERROR,"注册失败");
        }

    }


    //用户注销
    @RequestMapping(value = "/outlogin",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public commonReturnType outlogin(@RequestParam(name="a")String a
    ) throws Exception {
        //验证手机号和对应的otpCode相符合
//        String inSessionCode = (String) this.httpServletRequest.getSession().getAttribute(telephone);
//        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionCode)){
//            throw  new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
//        }
//用户注册流程
//        JSONObject jsonObject = HttpApi.getInstance().OUTLINE();
//        if(jsonObject.equals(200)) {
            if(1==1) {
                this.httpServletRequest.getSession().setAttribute("user_login",null);
                return commonReturnType.create(null);
            }else {
                throw new BusinessException(EmBusinessError.OUTLINE_ERROR,"注销失败");
            }
        }





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
