package com.xxl.job.executor.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.executor.service.PushTelegramServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.kenz.utils.StringUtils;

/**
 * 推送telegrame（Bean模式）
 *
 */
@Component
public class PronXxlJob {
    private static Logger logger = LoggerFactory.getLogger(PronXxlJob.class);

    @Autowired
    private PushTelegramServer pushTelegramServer;

    /**
     * 输入Cookic
     * 第1位为密码，,分隔后面的为解析地址
     */
    @XxlJob("getPronVideoByCookic")
    public ReturnT<String> demoJobHandler(String param) throws Exception {
        if (StringUtils.isEmpty(param) || param.split(",").length < 2) {
            XxlJobLogger.log("参数不正确");
            return ReturnT.FAIL;
        }
        String[] params = param.split(",");
        pushTelegramServer.pushPage(params[0],params[1]);
        return ReturnT.SUCCESS;
    }
    /**
     * 没有密码，自动登陆
     * 前两个参数为账号和密码
     * ,分隔解析地址
     */
    @XxlJob("getPronVideo")
    public ReturnT<String> pushPageFail(String param) throws Exception {
        if (StringUtils.isEmpty(param) || param.split(",").length < 1) {
            XxlJobLogger.log("参数不正确");
            return ReturnT.FAIL;
        }
        String[] params = param.split(",");
        pushTelegramServer.pushPageFail(params[0],params[1]);
        return ReturnT.SUCCESS;
    }

    public void init(){
        logger.info("init");
    }
    public void destroy(){
        logger.info("destory");
    }


}
