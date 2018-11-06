package com.example.demodbm.controller;


import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 借款订单扣款记录表 前端控制器
 * </p>
 *
 * @author kenzhao
 * @since 2018-09-10
 */
@RestController
@Log
public class IndexController {


  @GetMapping("/index")
  private String index(){

    log.info("request index ");
    return "index";
  }
}

