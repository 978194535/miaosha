package com.miaoshaoproject.service.Model;

public class OrderModel {
    //订单号
    private String id;

    //购买用户的Id
    private Integer userId;

    //商品的id
    private Integer itemId;

    //购买商品的单价秒杀的价格
    private Double itemPrice;

    //若非空,则表示是以秒杀商品的方式下单
    private Integer promoId;

    //数量
    private Integer amount;

    //购买金额若非空,则表示是以秒杀商品的方式下单
    private Double orderPrice;

    public String getId() {
        return id;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }
}
