package com.twocache.demo.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Bean
    @Primary
    public LettuceConnectionFactory redis1LettuceConnectionFactory(RedisStandaloneConfiguration redis1RedisConfig,
                                                                    GenericObjectPoolConfig redis1PoolConfig) {
        LettuceClientConfiguration clientConfig =
                LettucePoolingClientConfiguration.builder().commandTimeout(Duration.ofMillis(100))
                        .poolConfig(redis1PoolConfig).build();
        return new LettuceConnectionFactory(redis1RedisConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<String, String> redis1Template(
            @Qualifier("redis1LettuceConnectionFactory") LettuceConnectionFactory redis1LettuceConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        //开启事务
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(redis1LettuceConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Configuration
    public static class Redis1Config {
        @Value("${spring.redis1.host}")
        private String host;
        @Value("${spring.redis1.port}")
        private Integer port;
        @Value("${spring.redis1.password}")
        private String password;
        @Value("${spring.redis1.database}")
        private Integer database;

        @Value("${spring.redis1.lettuce.pool.max-active}")
        private Integer maxActive;
        @Value("${spring.redis1.lettuce.pool.max-idle}")
        private Integer maxIdle;
        @Value("${spring.redis1.lettuce.pool.max-wait}")
        private Long maxWait;
        @Value("${spring.redis1.lettuce.pool.min-idle}")
        private Integer minIdle;

        @Bean
        public GenericObjectPoolConfig redis1PoolConfig() {
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setMaxTotal(maxActive);
            config.setMaxIdle(maxIdle);
            config.setMinIdle(minIdle);
            config.setMaxWaitMillis(maxWait);
            return config;
        }

        @Bean
        public RedisStandaloneConfiguration redis1RedisConfig() {
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
            config.setHostName(host);
            config.setPassword(RedisPassword.of(password));
            config.setPort(port);
            config.setDatabase(database);
            return config;
        }
    }
}