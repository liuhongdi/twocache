package com.twocache.demo.controller;

import com.twocache.demo.pojo.Goods;
import com.twocache.demo.service.GoodsService;
import com.github.benmanes.caffeine.cache.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Resource
    private GoodsService goodsService;

    //商品详情 参数:商品id
    @Cacheable(value = "goods", key="#goodsId",sync = true)
    @GetMapping("/goodsget")
    @ResponseBody
    public Goods goodsInfo(@RequestParam(value="goodsid",required = true,defaultValue = "0") Long goodsId) {
        Goods goods = goodsService.getOneGoodsById(goodsId);
        return goods;
    }

}

