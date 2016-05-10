package com.example.max.iGo.Model;

import java.io.Serializable;
import java.net.URL;

/**
 * 确认订单页面（ConfirmedOrder）的数据类。
 */
public class OrderMessageModel implements Serializable {
    private String goodsName;
    private String price;
    private String salesVolume;
    private String imgUrl;
    private String orderVolum;
    private boolean isSure;

    public OrderMessageModel(String goodsName, String price, String salesVolume, String imgUrl, String orderVolum) {
        this.goodsName = goodsName;
        this.price = price;
        this.imgUrl = imgUrl;
        this.salesVolume = salesVolume;
        this.orderVolum = orderVolum;
    }
    public OrderMessageModel(String goodsName, String price, String salesVolume, String imgUrl) {
        this.goodsName = goodsName;
        this.price = price;
        this.imgUrl = imgUrl;
        this.salesVolume = salesVolume;
        this.orderVolum = "1";
    }
    public boolean isSure() {
        return isSure;
    }
    public void setIsSure(boolean isSure) {
        this.isSure = isSure;
    }
    public String getPrice() {
        return price;
    }

    public String getSalesVolume() {
        return salesVolume;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getOrderVolum() {
        return orderVolum;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSalesVolume(String salesVolume) {
        this.salesVolume = salesVolume;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setOrderVolum(String orderVolum) {
        this.orderVolum = orderVolum;
    }

    public String getGoodsName() {
        return goodsName;
    }
}
