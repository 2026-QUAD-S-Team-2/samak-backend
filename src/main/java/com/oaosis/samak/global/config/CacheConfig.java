package com.oaosis.samak.global.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager ehCacheManager() {
        javax.cache.CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();

        MutableConfiguration<Object, Object> config = new MutableConfiguration<>()
                .setStatisticsEnabled(true)
                .setStoreByValue(false) //store by reference
                .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_DAY)); //TTL 설정

        createCacheIfNotExists(cacheManager, "quizzes", config);
        createCacheIfNotExists(cacheManager, "news", config);
        createCacheIfNotExists(cacheManager, "guides", config);

        return new JCacheCacheManager(cacheManager);
    }

    private void createCacheIfNotExists(javax.cache.CacheManager cacheManager,
                                        String cacheName,
                                        MutableConfiguration<Object, Object> config) {
        if (cacheManager.getCache(cacheName) == null) {
            cacheManager.createCache(cacheName, config);
        }
    }
}