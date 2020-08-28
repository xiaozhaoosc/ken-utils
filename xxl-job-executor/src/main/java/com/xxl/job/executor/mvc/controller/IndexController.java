package com.xxl.job.executor.mvc.controller;

import com.xxl.job.executor.service.PushTelegramServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class IndexController {

    @Autowired
    private PushTelegramServer pushTelegramServer;

    private static Logger logger = LoggerFactory.getLogger(IndexController.class);
    @RequestMapping("/")
    @ResponseBody
    String index() {
        return "xxl job executor running.";
    }

    @RequestMapping("/test")
    @ResponseBody
    String test() {
        pushTelegramServer.pushPage("2020082612mzitu详细内容","xxx/sendMediaGroup");
        return "ok";
    }
    @RequestMapping("/pushPageFail")
    @ResponseBody
    String pushPageFail() {
        pushTelegramServer.pushPageFail("2020082612mzitu详细内容","xxx/sendPhoto");
        return "ok";
    }
}