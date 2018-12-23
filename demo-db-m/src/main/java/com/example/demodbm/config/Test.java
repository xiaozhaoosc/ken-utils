package com.example.demodbm.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Description:
 *
 * @author kenzhao
 * @date 2018/12/22 16:47
 */

public class Test {

  public static void main(String[] args) {
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime time = LocalDateTime.now();
    String localTime = df.format(time);
    LocalDateTime ldt = LocalDateTime.parse("2018-01-12 17:07:05",df);
    System.out.println("LocalDateTime转成String类型的时间："+localTime);
    System.out.println("String类型的时间转成LocalDateTime："+ldt);
  }
}