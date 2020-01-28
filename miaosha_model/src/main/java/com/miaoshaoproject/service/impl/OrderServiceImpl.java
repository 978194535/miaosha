package com.miaoshaoproject.service.impl;

import com.miaoshaoproject.service.ItemService;
import com.miaoshaoproject.service.Model.ItemModel;
import com.miaoshaoproject.service.Model.OrderModel;
import com.miaoshaoproject.service.Model.userModel;
import com.miaoshaoproject.service.OrderService;
import com.miaoshaoproject.service.userService;
import com.miaoshaproject.dao.OrderDaoMapper;
import com.miaoshaproject.dao.SequenceDaoMapper;
import com.miaoshaproject.dataobjcet.OrderDao;
import com.miaoshaproject.dataobjcet.SequenceDao;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
   private SequenceDaoMapper sequenceDaoMapper;
    @Autowired
    private ItemService itemService;
    @Autowired
    private userService userService1;
    @Autowired
    private OrderDaoMapper orderDaoMapper;
    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException {
        //校验下单状态
       ItemModel itemModel =  itemService.getItemById(itemId);
       if(itemModel==null){
           throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"商品信息不存在");
       }
        userModel userModel =  userService1.getUserById(userId);
       if(userModel==null){
           throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户不存在");
       }
       if(amount<=0||amount>99){
           throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"数量信息不正确");
       }
       //校验活动信息
        if(promoId!=null){
            //(1)校验对应活动是否存在这个适用商品
            if(promoId.intValue()!=itemModel.getPromoModel().getId()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动信息不正确");
                //校验活动是否在进行中
            }else if(itemModel.getPromoModel().getStatus().intValue()!=2){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动不在进行中");
            }
        }
        //落单减库存
        boolean result = itemService.decreaseStock(itemId,amount);
       if(!result){
            throw  new BusinessException(EmBusinessError.STCOK_NOT_ENOUGH);
       }
        //订单入库
        OrderModel orderModel = new OrderModel();
       orderModel.setAmount(amount);
       orderModel.setItemId(itemId);
       orderModel.setUserId(userId);
       if(promoId!=null){
        orderModel.setItemPrice((itemModel.getPromoModel().getPromoItemPrice()).doubleValue());
       }else {
           orderModel.setItemPrice(itemModel.getPrice());
       }

       orderModel.setPromoId(promoId);
       orderModel.setOrderPrice((orderModel.getItemPrice())*(amount));
       //生成交易流水号
        orderModel.setId(generateOrderNo());
       OrderDao orderDao = this.convertOrderDaoFromOderModel(orderModel);
       orderDaoMapper.insertSelective(orderDao);
       //加上商品销量
        itemService.increaseSales(itemId,amount);
        //返回前段

        return orderModel;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private String generateOrderNo(){
        //订单号有16位,
        StringBuilder stringBuilder = new StringBuilder();
        // 前8位为年月日
        LocalDateTime now = LocalDateTime.now();
       String nowdata = now.format(DateTimeFormatter.ISO_DATE).replace("-","");
       stringBuilder.append(nowdata);
        //中间6位为自增序列
        int sequence = 0;
        SequenceDao sequenceDao =  sequenceDaoMapper.getSequenceByName("order_info");
        sequence = sequenceDao.getCurrentValue();
        sequenceDao.setCurrentValue(sequenceDao.getCurrentValue()+sequenceDao.getStep());
        sequenceDaoMapper.updateByPrimaryKeySelective(sequenceDao);
        String sequenceStr = String.valueOf(sequence);
        for(int i =0;i<6-sequenceStr.length();i++){
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);
        //最后2位为分库分表位
        stringBuilder.append("00");
        return  stringBuilder.toString();

    }
    private OrderDao convertOrderDaoFromOderModel(OrderModel orderModel){
        if(orderModel==null){
            return null;
        }
        OrderDao orderDao =new OrderDao();
        BeanUtils.copyProperties(orderModel,orderDao);
        return orderDao;
    }
}
