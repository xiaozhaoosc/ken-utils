package com.example.demodbm.config;

import org.springframework.context.ApplicationContext;

/**
 * Description:springcontext 手动注入
 *  手动注入式，在启动类的main方法中添加
 *  ApplicationContext ctx = SpringApplication.run(Test.class, args);
 *   SpringUtil.setApplicationContext(ctx);  这个对应于AnoStatic
 * @author kenzhao
 * @date 2019/1/31 16:13
 */
public class SpringUtil {
  private static ApplicationContext applicationContext = null;

  public static void setApplicationContext(ApplicationContext applicationContext){
    if(SpringUtil.applicationContext == null){
      SpringUtil.applicationContext  = applicationContext;
    }

  }

  //获取applicationContext
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  //通过name获取 Bean.
  public static Object getBean(String name){
    return getApplicationContext().getBean(name);

  }

  //通过class获取Bean.
  public static <T> T getBean(Class<T> clazz){
    return getApplicationContext().getBean(clazz);
  }

  //通过name,以及Clazz返回指定的Bean
  public static <T> T getBean(String name,Class<T> clazz){
    return getApplicationContext().getBean(name, clazz);
  }
}