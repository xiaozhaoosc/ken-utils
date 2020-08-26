package com.xxl.job.executor.service;

public interface PushTelegramServer {
    /**
     * 正常按组推送
     * @param tableName
     * @param url
     */
    void pushPage(String tableName,String url);

    /**
     * 失败的单个推送推送
     * @param tableName
     * @param url
     */
    void pushPageFail(String tableName,String url);

}
