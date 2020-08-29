package com.xxl.job.executor.mvc.controller;

import com.xxl.job.executor.service.PronVideoServer;
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
    @Autowired
    private PronVideoServer pronVideoServer;

    private static Logger logger = LoggerFactory.getLogger(IndexController.class);
    @RequestMapping("/")
    @ResponseBody
    String index() {
        return "xxl job executor running.";
    }

    @RequestMapping("/test")
    @ResponseBody
    String test() {
        String param = "ua=fa951fa6cd6a040efa24debcc1e745cd; platform_cookie_reset=pc; platform=pc; bs=ngduxz9qyp0exphobipkum7gkgioyikl; ss=366429587298295058; fg_9d12f2b2865de2f8c67706feaa332230=33622.100000; _ga=GA1.2.743455686.1598673960; _gid=GA1.2.1094844638.1598673960; il=v16xxqZypvLcHbaDKcuLRivBRSJGtxHxf_QetsORBlLTwxNjA2NDUwMDU0c2VuekgydVIycWJYd3lHTWdrZkJKc2E2WUx2VnN4SG0zVmkweEFzMw..; expiredEnterModalShown=1," +
                "https://cn.pornhub.com/pornstar/veronica-leal,https://cn.pornhub.com/pornstar/jessa-rhodes,https://cn.pornhub.com/pornstar/rae-lil-black,https://cn.pornhub.com/pornstar/ai-uehara";
//                "https://cn.pornhub.com/categories/hentai,https://cn.pornhub.com/video?c=502,https://cn.pornhub.com/video/incategories/compilation/female-orgasm,https://cn.pornhub.com/video/incategories/female-orgasm/japanese";
        pronVideoServer.getVideosByCookie(param.split(","));
        return "ok";
    }
    @RequestMapping("/pushPageFail")
    @ResponseBody
    String pushPageFail() {
        pushTelegramServer.pushPageFail("2020082612mzitu详细内容","xxx/sendPhoto");
        return "ok";
    }
}