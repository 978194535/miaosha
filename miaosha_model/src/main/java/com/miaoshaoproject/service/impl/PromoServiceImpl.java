package com.miaoshaoproject.service.impl;

import com.miaoshaoproject.service.Model.PromoModel;
import com.miaoshaoproject.service.PromoService;
import com.miaoshaproject.dao.PromoDaoMapper;
import com.miaoshaproject.dataobjcet.PromoDao;

import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PromoServiceImpl implements PromoService {
    @Autowired
    private PromoDaoMapper promoDaoMapper;
    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        //获取对应商品的秒杀活动信息
        PromoDao promoDao = promoDaoMapper.selectByItemId(itemId);
        //dataobject->PromoModel
        PromoModel promoModel = this.convertModelFrom(promoDao);
        //判断当前时间是否秒杀活动即将开始或正在进行
        if (promoModel == null) {

            return null;
        }
        if(promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        }else if (promoModel.getEndDate().isBeforeNow()){
            promoModel.setStatus(3);
        }else {
            promoModel.setStatus(2);
        }
        return promoModel;
    }
    private PromoModel convertModelFrom(PromoDao promoDao){
        if(promoDao==null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDao,promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDao.getPromoItemPrice()));
        promoModel.setStartDate(new DateTime(promoDao.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDao.getEndDate()));
        return promoModel;
    }
}
