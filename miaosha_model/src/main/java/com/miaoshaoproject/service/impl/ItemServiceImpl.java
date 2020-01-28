package com.miaoshaoproject.service.impl;

import com.miaoshaoproject.service.ItemService;
import com.miaoshaoproject.service.Model.ItemModel;
import com.miaoshaoproject.service.Model.PromoModel;
import com.miaoshaoproject.service.PromoService;
import com.miaoshaproject.dao.ItemDaoMapper;
import com.miaoshaproject.dao.ItemStockDaoMapper;
import com.miaoshaproject.dataobjcet.ItemDao;
import com.miaoshaproject.dataobjcet.ItemStockDao;
import com.miaoshaproject.error.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private PromoService promoService;
    @Autowired
    private ItemDaoMapper itemDaoMapper;
    @Autowired
    private ItemStockDaoMapper itemStockDaoMapper;

    private ItemStockDao convertItemStockFromItemModel(ItemModel itemModel){
        if(itemModel == null){
            return  null;
        }
        ItemStockDao itemStockDao = new ItemStockDao();
        itemStockDao.setStock(itemModel.getStock());
        itemStockDao.setUserId(itemModel.getId());
        return itemStockDao;
    }
    private ItemDao convertItemDaoFromItemModel(ItemModel itemModel){
        if(itemModel == null){
            return  null;
        }
        ItemDao itemDao = new ItemDao();
        BeanUtils.copyProperties(itemModel,itemDao);
        return itemDao;
    }
    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) {

        //转化ItemModel为dataobject
        ItemDao itemDao = this.convertItemDaoFromItemModel(itemModel);
        //写入数据库
        itemDaoMapper.insertSelective(itemDao);
        itemModel.setId(itemDao.getId());
        ItemStockDao itemStockDao = this.convertItemStockFromItemModel(itemModel);
        itemStockDaoMapper.insertSelective(itemStockDao);


        //返回创建完成的对象
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDao> list = itemDaoMapper.listItem();
        List<ItemModel> list1 = new ArrayList<ItemModel>();
        for (int i = 0; i < list.size(); i++) {
            ItemDao itemDao =  list.get(i);
            ItemModel itemModel = new ItemModel();
            BeanUtils.copyProperties(itemDao,itemModel);
            list1.add(itemModel);
        }
        return  list1;
    }

    @Override
    public ItemModel getItemById(Integer Id) {
        ItemDao itemDao = itemDaoMapper.selectByPrimaryKey(Id);
        if (itemDao == null){
            return  null;
        }
        //操作获得数量
       ItemStockDao itemStockDao =  itemStockDaoMapper.selectByItemId(itemDao.getId() );
        //转换模型
        ItemModel itemModel =this.convertModelFromData(itemDao,itemStockDao);
        //获取活动商品信息
        PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());
        if(promoModel!=null&&promoModel.getStatus().intValue()!=3){
           itemModel.setPromoModel(promoModel);
        }
        return itemModel;
    }

    @Override
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
        int affected = itemStockDaoMapper.decreaseStock(itemId,amount);
        if(affected>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        itemDaoMapper.increaseSales(itemId,amount);
    }

    private ItemModel convertModelFromData(ItemDao itemDao,ItemStockDao itemStockDao){
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDao,itemModel);
        itemModel.setStock(itemStockDao.getStock());
        return  itemModel;
    }
}
