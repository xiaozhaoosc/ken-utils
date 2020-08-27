package com.xxl.job.executor.service.impl;

import com.google.gson.JsonObject;
import com.xxl.job.executor.dao.MzituDao;
import com.xxl.job.executor.model.Mzitu;
import com.xxl.job.executor.mvc.controller.IndexController;
import com.xxl.job.executor.service.PushTelegramServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.kenz.utils.ApacheHttpClientUtils;
import site.kenz.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Telegram 机器人发消息
 */
@Service
public class PushTelegramServerImpl implements PushTelegramServer {
    private static Logger logger = LoggerFactory.getLogger(PushTelegramServerImpl.class);
    @Autowired
    private MzituDao mzituDao;

    /**
     * 正常按组推送
     *
     * @param tableName
     * @param url
     */
    @Override
    public void pushPage(String tableName, String url) {
//        List<Mzitu> mzitus = mzituDao.pageList(0,4,"2020082612mzitu详细内容");
        List<Mzitu> mzitus = mzituDao.pageList(0,4,tableName);
        if (mzitus != null && mzitus.size() > 0) {
//            String url3 = "https://api.telegram.org/bot9139462568:AAGS2h1IU_xhRoimvX5DUr8RVjq8Gd3C77g/sendMediaGroup";
            JsonObject jsonObject = new JsonObject();
            //https://t.me/meitujianshang
            jsonObject.addProperty("chat_id","@meitujianshang");
//        jsonObject.addProperty("photo","https://cdn.jsdelivr.net/gh/zmzt/202001/77973adb3574bd3125a250bdf79e94ee.jpg,https://cdn.jsdelivr.net/gh/zmzt/202001/0194aebf7bfa8c86f962f234dd29a6df.jpg,https://cdn.jsdelivr.net/gh/zmzt/202001/4dbf5813305bee0931cb6b1fe079e37d.jpg,https://cdn.jsdelivr.net/gh/zmzt/202001/fae670ebf338599deb773401779165dd.jpg");

            List<JsonObject> list = new ArrayList<JsonObject>();

            mzitus.forEach(mzitu -> {
//                logger.info("job group name" + mzitu.getImgURL()+ mzitu.getDownloadTime());
                JsonObject jsonObject1 = new JsonObject();
                jsonObject1.addProperty("type","photo");
                jsonObject1.addProperty("media",mzitu.getImgURL());
                list.add(jsonObject1);
            });

            jsonObject.addProperty("media", GsonUtil.objectToJson(list));
//        jsonObject.addProperty("caption","1");
//        jsonObject.addProperty("parse_mode","1");
//        jsonObject.addProperty("disable_notification","1");
//        jsonObject.addProperty("reply_to_message_id","1");
//        jsonObject.addProperty("reply_markup","1");
            String json = jsonObject.toString();
//            logger.info("http post jsonObject:" + json);
            String response = null;
            try {
                response = ApacheHttpClientUtils.sendPost(url, json);
            } catch (Exception exception) {
                exception.printStackTrace();
                mzitus.forEach(mzitu -> {
//                logger.info("job group name" + mzitu.getImgURL()+ mzitu.getDownloadTime());
                    mzituDao.updateFail(mzitu.getId(), "2020082612mzitu详细内容");
                });
                logger.error("http post response:" + exception.getMessage());
                return;
            }
//            logger.info("http post response:" + response);

            mzitus.forEach(mzitu -> {
//                logger.info("job group name" + mzitu.getImgURL()+ mzitu.getDownloadTime());
                mzituDao.update(mzitu.getId(), "2020082612mzitu详细内容");
            });

        }
    }

    /**
     * 失败的单个推送推送
     *
     * @param tableName
     * @param url
     */
    @Override
    public void pushPageFail(String tableName, String url) {
        List<Mzitu> mzitus = mzituDao.pageListFail(0,1,tableName);
        if (mzitus != null && mzitus.size() > 0) {
            mzitus.forEach(mzitu -> {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("chat_id","@meitujianshang");
                jsonObject.addProperty("photo",mzitu.getImgURL());
                try {
                    ApacheHttpClientUtils.sendPost(url, jsonObject.toString());
                } catch (Exception exception) {
                    logger.error("http post response:" + exception.getMessage());
                    mzituDao.updateFail(mzitu.getId(), tableName);
                }
                mzituDao.update(mzitu.getId(), tableName);
            });


        }
    }
}
