package com.example.demodbm;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@MapperScan("com.example.demodbm.business.dao")
public class DemoDbMApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoDbMApplication.class, args);
  }
}
