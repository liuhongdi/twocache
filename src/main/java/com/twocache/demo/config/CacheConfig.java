package com.twocache.demo.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Profile("prod")   //prod这个profile时缓存才生效
@Configuration
@EnableCaching //开启缓存
public class CacheConfig {
    public static final int DEFAULT_MAXSIZE = 10000;
    public static final int DEFAULT_TTL = 600;


    /**
     * 定义cache名称、超时时长（秒）、最大容量
     * 每个cache缺省：600秒超时、最多缓存10000条数据
     */
    public enum CacheEnum{
        goods(600,1000),          //有效期600秒
        homePage(7200,1000),  //有效期2个小时 , 最大容量1000
        ;
        CacheEnum(int ttl, int maxSize) {
            this.ttl = ttl;
            this.maxSize = maxSize;
        }
        private int maxSize=DEFAULT_MAXSIZE;	//最大數量
        private int ttl=DEFAULT_TTL;		//过期时间（秒）
        public int getMaxSize() {
            return maxSize;
        }
        public int getTtl() {
            return ttl;
        }
    }


    //创建基于Caffeine的Cache Manager
    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        ArrayList<CaffeineCache> caches = new ArrayList<CaffeineCache>();
        for(CacheEnum c : CacheEnum.values()){
            caches.add(new CaffeineCache(c.name(),
                    Caffeine.newBuilder().recordStats()
                            .expireAfterWrite(c.getTtl(), TimeUnit.SECONDS)
                            .maximumSize(c.getMaxSize())
                            .build())
            );
        }
        cacheManager.setCaches(caches);
        return cacheManager;
    }
}
