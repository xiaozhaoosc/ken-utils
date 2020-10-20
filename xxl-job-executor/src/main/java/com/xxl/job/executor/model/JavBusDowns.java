package com.xxl.job.executor.model;

import lombok.Data;

import javax.persistence.Entity;

/**
 * Created by xuxueli on 16/9/30.
 */
@Data
@Entity
public class JavBusDowns extends BaseClass{

    private String rootUrl="https://www.javbus.com";

    //详情页面
    private String detailUrl;
    //影片海报地址
    private String detailImgURL;
    //影片海报名称
    private String detailName;
    //影片标签
    private String tags;
    //分类   <span class="genre"><a href="https://www.javbus.com/genre/e">巨乳</a></span>
    private String genre;
    //分类名称
    private String genreName;
    //<span class="genre" onmouseover="hoverdiv(event,'star_q8t')" onmouseout="hoverdiv(event,'star_q8t')"> <a href="https://www.javbus.com/star/q8t">河北はるな</a> </span>
//            log.info(genre.select("a").select("font").select("font").text());
    //明星 链接
    private String starUrl;
    //明星 名称
    private String starName;
    //下载地址
    private String downUrl;
    //标签
    private String downTags;
    //大小
    private String fileSize;
    //时间，有效期
    private String fileEndTime;
}
