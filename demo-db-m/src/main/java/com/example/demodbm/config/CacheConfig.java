package com.example.demodbm.config;

import java.lang.reflect.Method;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
/**
 * Description: 缓存管理工具配置
 *
 * @author kenzhao
 * @date 2019/1/30 17:15
 */
@Configuration
public class CacheConfig extends CachingConfigurerSupport{

  private final RedisTemplate redisTemplate;
  private Long timeToLive = 60L;
  @Autowired
  private Environment environment;


  @Bean
  public CacheManager cacheManager(RedisConnectionFactory factory) {
//    // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
//    RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
//    // 设置缓存的默认过期时间，也是使用Duration设置
//    config = config.entryTtl(Duration.ofMinutes(3L))
//        .disableCachingNullValues();     // 不缓存空值
//
//    // 设置一个初始化的缓存空间set集合
//    Set<String> cacheNames = new HashSet<>();
//    cacheNames.add("my-redis-cache1");
//    cacheNames.add("my-redis-cache2");
//    cacheNames.add("my-redis-user");
//
//    // 对每个缓存空间应用不同的配置
//    Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
//    configMap.put("my-redis-cache1", config);
//    configMap.put("my-redis-user", config);
//    configMap.put("my-redis-cache2", config.entryTtl(Duration.ofSeconds(120)));
//
//    RedisCacheManager cacheManager = RedisCacheManager.builder(factory)     // 使用自定义的缓存配置初始化一个cacheManager
//        .initialCacheNames(cacheNames)  // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
//        .withInitialCacheConfigurations(configMap)
//        .build();
    GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
    //使用fastjson序列化
//    FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration
            .ofSeconds(new Long(environment.getProperty("cache.redis.timeToLive", "60"))))
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer))
        .serializeValuesWith(RedisSerializationContext.SerializationPair
            .fromSerializer(genericJackson2JsonRedisSerializer))
        .disableCachingNullValues()
        .computePrefixWith(cacheName -> environment.getProperty("spring.application.name").concat(":").concat(cacheName).concat(":"));
    CacheManager cacheManager = RedisCacheManager.builder(factory).cacheDefaults(cacheConfiguration).build();
    return cacheManager;
  }

  /**
   * key值为className+methodName+参数值列表
   * @return
   */
  @Override
  public KeyGenerator keyGenerator() {
    return new KeyGenerator() {
      @Override
      public Object generate(Object o, Method method, Object... args) {
        StringBuilder sb = new StringBuilder();
        sb.append(o.getClass().getName()).append("#");
        sb.append(method.getName()).append("(");
        for (Object obj : args) {
          if(obj != null) { // 在可选参数未给出时时，会出现null，此时需要跳过
            sb.append(obj.toString()).append(",");
          }
        }
        if (sb.length() > 2) {
          return sb.substring(0, sb.length() - 2) + ")";
        }else{
          return sb.append(")").toString();
        }

      }
    };
  }


  @Autowired
  public CacheConfig(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Bean
  public RedisTemplate<Object,Object> redisTemplate(){
    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    // 设置key的序列化方式为String
    redisTemplate.setKeySerializer(stringRedisSerializer);
    redisTemplate.setHashKeySerializer(stringRedisSerializer);
    // 设置value的序列化方式为JSON
    GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
    redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
    redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);

    //使用fastjson序列化
//    FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
//    redisTemplate.setValueSerializer(fastJsonRedisSerializer);
//    redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
    return redisTemplate;
  }



}