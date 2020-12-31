package com.xxl.job.executor.mvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class IndexController {

    @Autowired
    private Environment environment;

    private static Logger logger = LoggerFactory.getLogger(IndexController.class);
    @RequestMapping("/")
    @ResponseBody
    String index() {
        return "xxl job executor running.";
    }

}