package com.example.demodbm.controller;


import com.example.demodbm.business.dto.User;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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


  @RequestMapping("/index")
  private String index(HttpServletRequest httpServletRequest,@RequestParam String params){
    httpServletRequest.getParameterMap();
    log.info("request index ");
    return "index";
  }
  @RequestMapping("/index2")
  private String index2(HttpServletRequest httpServletRequest,@RequestBody String params){
    httpServletRequest.getParameterMap();
    log.info("request index ");
    return "index";
  }
  @RequestMapping("/index3")
  private String index3(HttpServletRequest httpServletRequest,@RequestBody User user){
    httpServletRequest.getParameterMap();
    log.info("request index ");
    return "index";
  }
}

