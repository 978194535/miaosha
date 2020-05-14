package com.miaoshaproject.controller;

import com.miaoshaoproject.service.Model.OrderModel;
import com.miaoshaoproject.service.Model.userModel;
import com.miaoshaoproject.service.OrderService;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.commonReturnType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class OrderController extends BaseController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    //封装下单请求
    @RequestMapping(value = "/createorder",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public commonReturnType createOrder(@RequestParam(name = "itemId")Integer itemId,
                                        @RequestParam(name = "amount")Integer amount,
                                        @RequestParam(name = "promoId",required = false)Integer promoId) throws BusinessException {
        //获取用户的登录信息
        Boolean islogin =(Boolean) httpServletRequest.getSession().getAttribute("login");
        if(islogin == null|| !islogin.booleanValue()){
        throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        //获取登录的用户信息
        userModel userModel1 =(userModel) httpServletRequest.getSession().getAttribute("user_login");

        OrderModel orderModel = orderService.createOrder(userModel1.getId(),itemId,promoId,amount);
        return commonReturnType.create(null);

    }
    @RequestMapping(value = "/test",method = {RequestMethod.GET})
    public commonReturnType test() throws BusinessException {
        OrderModel orderModel = orderService.createOrder(1,1,1,1);
        return commonReturnType.create(null);

    }
}
