package com.twocache.demo.service.impl;

import com.twocache.demo.mapper.GoodsMapper;
import com.twocache.demo.pojo.Goods;
import com.twocache.demo.service.GoodsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private RedisTemplate redis1Template;

    @Value("${spring.redis1.enabled}")
    private Integer redis1enabled;
    //得到一件商品的信息
    @Override
    public Goods getOneGoodsById(Long goodsId) {
        Goods goodsOne;
        if (redis1enabled == 1) {
            System.out.println("get data from redis");
            Object goodsr = redis1Template.opsForValue().get("goods_"+String.valueOf(goodsId));
            if (goodsr == null) {
                System.out.println("get data from mysql");
                goodsOne = goodsMapper.selectOneGoods(goodsId);
                if (goodsOne == null) {
                    redis1Template.opsForValue().set("goods_"+String.valueOf(goodsId),"-1",600, TimeUnit.SECONDS);
                } else {
                    redis1Template.opsForValue().set("goods_"+String.valueOf(goodsId),goodsOne,600, TimeUnit.SECONDS);
                }
            } else {
                if (goodsr.equals("-1")) {
                    goodsOne = null;
                } else {
                    goodsOne = (Goods)goodsr;
                }
            }
        } else {
            System.out.println("get data from mysql");
            goodsOne = goodsMapper.selectOneGoods(goodsId);
        }
        return goodsOne;
    }
}
