package com.miaoshaoproject.service;

import com.miaoshaoproject.service.Model.ItemModel;
import com.miaoshaproject.error.BusinessException;


import java.util.List;


public interface ItemService {
    //创建商品
    ItemModel createItem(ItemModel itemModel);
    //商品列表的浏览
    List<ItemModel> listItem();
    //商品详情浏览
    ItemModel getItemById(Integer Id);

    boolean decreaseStock(Integer itemId, Integer amount)throws BusinessException;

    //商品销量增加
    void increaseSales(Integer itemId, Integer amount)throws BusinessException;
}
