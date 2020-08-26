package com.xxl.job.executor.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.ShardingUtil;
import com.xxl.job.executor.mvc.controller.IndexController;
import com.xxl.job.executor.service.PushTelegramServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.kenz.utils.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 推送telegrame（Bean模式）
 *
 */
@Component
public class PushTelegramXxlJob {
    private static Logger logger = LoggerFactory.getLogger(PushTelegramXxlJob.class);

    @Autowired
    private PushTelegramServer pushTelegramServer;

    /**
     * 正常组推送
     */
    @XxlJob("pushPageJobHandler")
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
     * 失败的单个推送推送
     */
    @XxlJob("pushPageFailJobHandler")
    public ReturnT<String> pushPageFail(String param) throws Exception {
        if (StringUtils.isEmpty(param) || param.split(",").length < 2) {
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
