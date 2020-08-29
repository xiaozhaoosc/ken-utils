package com.xxl.job.executor.model;

import lombok.Data;

/**
 * Created by xuxueli on 16/9/30.
 */
@Data
public class Pron {
    private int id;
    private String titleName;
    private String title;
    private String url;
    private int valid;
    private int status;
    private int viewsNum;
    private String viewsNumStr;
    private String parentUrl;
}
