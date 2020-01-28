package com.miaoshaoproject.service;

import com.miaoshaoproject.service.Model.OrderModel;
import com.miaoshaproject.error.BusinessException;



public interface OrderService {
    //使用1.通过前端上穿过来的活动ID,然后下单接口内校验对应ID是否属于对应商品切活动已经开始
    //2.直接在下单接口内判断对应的商品是否存在秒杀活动,若存在进行中的则以秒杀的价格下单
    OrderModel createOrder(Integer userId, Integer promoId, Integer itemId, Integer amount) throws BusinessException;
}
