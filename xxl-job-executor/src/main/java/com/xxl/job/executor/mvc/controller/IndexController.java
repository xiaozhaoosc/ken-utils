package com.xxl.job.executor.mvc.controller;

import com.xxl.job.executor.service.JavServer;
import com.xxl.job.executor.service.PronVideoServer;
import com.xxl.job.executor.service.PushTelegramServer;
import org.apache.http.client.fluent.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@EnableAutoConfiguration
public class IndexController {

    @Autowired
    private PushTelegramServer pushTelegramServer;
    @Autowired
    private PronVideoServer pronVideoServer;
    @Autowired
    private JavServer javServer;
    @Autowired
    private Environment environment;

    private static Logger logger = LoggerFactory.getLogger(IndexController.class);
//    @RequestMapping("/")
//    @ResponseBody
//    String index() {
//        return "xxl job executor running.";
//    }

    @GetMapping(path = "/{invite}")
    @ResponseBody
    void index(@PathVariable(name = "invite") String invite, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(301);
//        wzef2ku
        logger.info(invite);
        String url = environment.getProperty("kuailianurl", "https://www.kuailian.link/invite/register.html?invite=") + invite;
        response.setHeader("Location",url);
//---分割线---
        response.sendRedirect(url);
    }

    @RequestMapping("/jav")
    @ResponseBody
    String jav() {
//        System.setProperty("socksProxyHost", "127.0.0.1");
//        System.setProperty("socksProxyPort", "10808");
//        String param = "ua=fa951fa6cd6a040efa24debcc1e745cd; platform_cookie_reset=pc; platform=pc; bs=ngduxz9qyp0exphobipkum7gkgioyikl; ss=366429587298295058; fg_9d12f2b2865de2f8c67706feaa332230=33622.100000; _ga=GA1.2.743455686.1598673960; _gid=GA1.2.1094844638.1598673960; il=v16xxqZypvLcHbaDKcuLRivBRSJGtxHxf_QetsORBlLTwxNjA2NDUwMDU0c2VuekgydVIycWJYd3lHTWdrZkJKc2E2WUx2VnN4SG0zVmkweEFzMw..; expiredEnterModalShown=1," +
//                "https://cn.pornhub.com/pornstars?age=18-30";
//        String param = "https://www.javbus.com/genre/sub";
        String param = "https://www.busfan.one/series/i8k";
        javServer.getVideosByCookie(param.split(","));
        return "ok";
    }

    @RequestMapping("/test")
    @ResponseBody
    String test() {
//        System.setProperty("socksProxyHost", "127.0.0.1");
//        System.setProperty("socksProxyPort", "10808");
//        String param = "ua=fa951fa6cd6a040efa24debcc1e745cd; platform_cookie_reset=pc; platform=pc; bs=ngduxz9qyp0exphobipkum7gkgioyikl; ss=366429587298295058; fg_9d12f2b2865de2f8c67706feaa332230=33622.100000; _ga=GA1.2.743455686.1598673960; _gid=GA1.2.1094844638.1598673960; il=v16xxqZypvLcHbaDKcuLRivBRSJGtxHxf_QetsORBlLTwxNjA2NDUwMDU0c2VuekgydVIycWJYd3lHTWdrZkJKc2E2WUx2VnN4SG0zVmkweEFzMw..; expiredEnterModalShown=1," +
//                "https://cn.pornhub.com/pornstars?age=18-30";
        String param = "https://cn.pornhub.com/categories/hentai,https://cn.pornhub.com/video?c=502,https://cn.pornhub.com/video/incategories/compilation/female-orgasm,https://cn.pornhub.com/video/incategories/female-orgasm/japanese";
        pronVideoServer.getVideosByCookie(param.split(","));
//        pronVideoServer.getVideosByCookieStar(param.split(","));
        return "ok";
    }
    @RequestMapping("/pushPageFail")
    @ResponseBody
    String pushPageFail() {
//        System.setProperty("socksProxyHost", "127.0.0.1");
//        System.setProperty("socksProxyPort", "10808");
        pushTelegramServer.pushPageFail("2020082612mzitu详细内容","xxx/sendPhoto");
        return "ok";
    }
}