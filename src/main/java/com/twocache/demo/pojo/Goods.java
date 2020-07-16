package com.twocache.demo.pojo;

import java.math.BigDecimal;

public class Goods {
    //商品id
    Long goodsId;
    public Long getGoodsId() {
        return this.goodsId;
    }
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    //商品名称
    private String goodsName;
    public String getGoodsName() {
        return this.goodsName;
    }
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    //商品标题
    private String subject;
    public String getSubject() {
        return this.subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    //商品价格
    private BigDecimal price;
    public BigDecimal getPrice() {
        return this.price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    //库存
    int stock;
    public int getStock() {
        return this.stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }

    public String toString(){
        return " Goods:goodsId=" + goodsId +" goodsName=" + goodsName+" subject=" + subject+" price=" + price+" stock=" + stock;
    }
}
