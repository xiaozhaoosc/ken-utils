package com.xxl.job.executor.service;

public interface PronVideoServer {
    /**
     * 输入密码的
     * @param params
     */
    void getVideosByCookie(String[] params);

    /**
     * 自动登陆的
     * 前两个参数为账号和密码
     * @param params
     */
    void getVideos(String[] params);

}
