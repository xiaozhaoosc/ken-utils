package com.example.demodbm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demodbm.business.entity.User;
import com.example.demodbm.business.service.IUserService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author kenzhao
 * @date 2019/1/30 18:29
 */

@RestController
@RequestMapping("/redisOpt")
@Slf4j
public class RedisController {


  @Autowired
  private RedisTemplate redisTemplate;
  @Autowired
  private IUserService userService;

  @RequestMapping("/setKey")
  private String setKey(@RequestParam String keyStr,@RequestParam String valueStr){
    ValueOperations valueOperations = redisTemplate.opsForValue();
    valueOperations.set(keyStr, valueStr);
    log.info("redisOpt setKey " + valueOperations.get(keyStr));
    return String.valueOf(valueOperations.get(keyStr));
  }
  @RequestMapping("/setUser")
  private Object setUser(@RequestParam String keyStr){
    ValueOperations valueOperations = redisTemplate.opsForValue();

    Gson gson = new Gson();

    if (valueOperations.get(keyStr) != null) {
      User user = (User) valueOperations.get(keyStr);
      log.info("RedisController getredis setUser setKey user:" + gson.toJson(user));
    }else{
      User user = userService.getUserInfo(keyStr);
      valueOperations.set(keyStr, user);
      log.info("RedisController getdb setUser setKey user:" + gson.toJson(user));
    }


    User user = (User)userService.getUserInfo(keyStr);
    valueOperations.set(keyStr, user);
    log.info("RedisController getdborcache setUser setKey user:" + gson.toJson(user));

    User user2 = (User)userService.getUserInfo2(keyStr);
    log.info("RedisController getdb setUser setKey user2:" + gson.toJson(user2));
    return valueOperations.get(keyStr);
  }
}