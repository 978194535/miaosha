package com.miaoshaproject.controller;

import com.miaoshaoproject.service.ItemService;
import com.miaoshaoproject.service.Model.ItemModel;
import com.miaoshaproject.config.*;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.commonReturnType;
import com.miaoshaproject.viewObject.ItemVo;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller("/item")
@RequestMapping("/item")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class ItemCollertor extends BaseController {
    @Autowired
    CacheTemplate cacheTemplate;
    @Autowired
    private ItemService itemService;
    @Autowired
    private RedisUtil redisUtil;
    //创建商品
    @RequestMapping(value = "/create",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public commonReturnType createItem(@RequestParam(name = "title")String title,
                                       @RequestParam(name = "description")String description,
                                       @RequestParam(name = "price")Double price,
                                       @RequestParam(name = "stock")Integer stock,
                                       @RequestParam(name = "imgUrl")String imgUrl){
        //封装service请求来创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setStock(stock);
        itemModel.setDescription(description);
        itemModel.setImgUrl(imgUrl);
        itemModel.setPrice(price);
        ItemModel itemModelFormReturn = itemService.createItem(itemModel);
        ItemVo itemVo = this.convert(itemModelFormReturn);
        return  commonReturnType.create(itemVo);
    }
    //获取商品
    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    @ResponseBody
    public commonReturnType getItem(@RequestParam(name = "id")Integer id) throws Exception {
//        RedisBloomFilter redisBloomFilter = new RedisBloomFilter();
//        if(!redisBloomFilter.isExist(String.valueOf(id))){
//            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
//        }
//        RedisLock redisLock = new RedisLock();
//        redisLock.lock(String.valueOf(id));
//        ItemModel itemModel = (ItemModel)redisUtil.get(String.valueOf(id));
//        if(itemModel==null){
//            ItemModel itemModel1 = itemService.getItemById(id);
//            ItemVo itemVo = this.convert(itemModel1);
//            redisLock.unlock(String.valueOf(id));
//            return commonReturnType.create(itemVo);
//        }else {
//            ItemVo itemVo = this.convert(itemModel);
//            redisLock.unlock(String.valueOf(id));
//            return commonReturnType.create(itemVo);
//        }
        return cacheTemplate.redisaa("ID", 10, TimeUnit.HOURS, new CacheLoadAble<ItemModel>() {
            @Override
            public ItemModel load() {
                return null;
            }
        });


    }




    //获取商品列表
    @RequestMapping(value = "/getlist",method = {RequestMethod.GET})
    @ResponseBody
    public  commonReturnType getItemlist(){
        List<ItemModel> list = itemService.listItem();
        List<ItemVo> list1 = new ArrayList<ItemVo>();
        for (int i = 0; i < list.size(); i++) {
            ItemModel itemModel =  list.get(i);
            ItemVo itemVo = new ItemVo();
            BeanUtils.copyProperties(itemModel,itemVo);
            list1.add(itemVo);
        }
        return commonReturnType.create(list1);
    }

    private ItemVo convert(ItemModel itemModel){
        if(itemModel==null){
            return null;
        }
        ItemVo itemVo = new ItemVo();
        BeanUtils.copyProperties(itemModel,itemVo);
        if(itemModel.getPromoModel()!=null){
            itemVo.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVo.setPromoId(itemModel.getPromoModel().getId());
            itemVo.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVo.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());

        }else {
            itemVo.setPromoStatus(0);

        }

        return itemVo;
    }
}
