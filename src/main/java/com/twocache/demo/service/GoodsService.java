package com.twocache.demo.service;

import com.twocache.demo.pojo.Goods;

public interface GoodsService {
    public Goods getOneGoodsById(Long goodsId);
}