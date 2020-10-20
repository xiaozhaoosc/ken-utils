package com.xxl.job.executor.service.impl;

import com.google.common.collect.Lists;
import com.xxl.job.executor.dao.JavBusDao;
import com.xxl.job.executor.dao.PronDao;
import com.xxl.job.executor.model.JavBus;
import com.xxl.job.executor.model.JavBusDowns;
import com.xxl.job.executor.repository.JavBusDownRepository;
import com.xxl.job.executor.repository.JavBusRepository;
import com.xxl.job.executor.service.JavServer;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import site.kenz.utils.ApacheHttpClientUtils;
import site.kenz.utils.SeleniumUtils;
import site.kenz.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class JavServerImpl implements JavServer {

    @Autowired
    private JavBusDao javBusDao;
    @Autowired
    private PronDao pronDao;
    @Autowired
    private JavBusRepository javBusRepository;
    @Autowired
    private JavBusDownRepository javBusDownRepository;

    /**
     * 输入密码的
     *
     * @param params
     */
    @Override
    public void getVideosByCookie(String[] params) {
        //请求首页
        try {
            postIndex(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postIndex(String[] params) throws Exception {
        String url = params[0];
        System.setProperty("socksProxyHost", "127.0.0.1");
        System.setProperty("socksProxyPort", "10808");
        String context = ApacheHttpClientUtils.sendGet(url);
        Document doc = Jsoup.parse(context);
//        Document doc = Jsoup.connect(url).get();
//        System.out.println(doc.title());
        Elements masonrys = doc.select(".item");

//        int i = 0;
        for (Element masonry:masonrys
             ) {
//            i++;
//            if (i > 2) {
//                return;
//            }
            Element movieBox = masonry.selectFirst(".movie-box");
            JavBus javBus = new JavBus();
            //详情页面
            javBus.setDetailUrl(movieBox.attr("href"));
            //影片海报地址
            javBus.setDetailImgURL(movieBox.select(".photo-frame").select("img").attr("src"));
            //影片海报名称
            javBus.setDetailName(movieBox.select(".photo-frame").select("img").attr("title"));

            Elements btnTags = movieBox.select(".photo-info").select(".btn");
            StringBuffer tags = new StringBuffer("  ");
            for (Element btn:btnTags
                 ) {
                //影片标签
                tags.append(btn.attr("title") + ",");
            }
            javBus.setTags(tags.substring(0,tags.length()-1));
            List<JavBusDowns> javBusList = Lists.newArrayList();
            javBus.setCreateTime(new Date());
            javBus.setUpdateTime(new Date());

            JavBusDowns javBusDowns = new JavBusDowns();
            javBusRepository.save(javBus);
            BeanUtils.copyProperties(javBus,javBusDowns);
            javBusList.add(javBusDowns);
            getDetailDownUrl(movieBox,javBusList);
            javBusDownRepository.saveAll(javBusList);

        }
//        String html = ApacheHttpClientUtils.sendGet(url, headParam);
//        System.out.println(html);

    }


    /**
     * 获取影片详情
     * 包含下载地址
     * @param movieBox
     */
    private void getDetail(Element movieBox) throws Exception {
        String contextSub = ApacheHttpClientUtils.sendGet(movieBox.attr("href"));
        Document docSub = Jsoup.parse(contextSub);
        Elements genre = docSub.select(".row").select(".genre");
        //分类   <span class="genre"><a href="https://www.javbus.com/genre/e">巨乳</a></span>
        log.info(genre.select("a").attr("href") );
        //分类名称
        log.info(genre.select("a").text());
        //<span class="genre" onmouseover="hoverdiv(event,'star_q8t')" onmouseout="hoverdiv(event,'star_q8t')"> <a href="https://www.javbus.com/star/q8t">河北はるな</a> </span>
//            log.info(genre.select("a").select("font").select("font").text());
        //明星 链接
        log.info(genre.last().select("a").attr("href"));
        //明星 名称
        log.info(genre.last().select("a").text());
//            String downParam = docSub.select("script").get(8).html();
//            String[] downParams = downParam.replaceAll("var","").replaceAll(" ","").replaceAll("\n","").replaceAll("\t","").replaceAll("'","").split(";");
//            log.info(downParam);
//            String downUrl = String.format("https://www.javbus.com/ajax/uncledatoolsbyajax.php?%s&lang=zh&img=%s&uc=%s&floor=%s",downParams[0],downParams[1],downParams[2]);

        //https://www.javbus.com/ajax/uncledatoolsbyajax.php?gid=44473963134&lang=zh&img=https://pics.javbus.com/cover/7vz6_b.jpg&uc=0&floor=688
//            Element table = docSub.select("tr[onmouseover]").select(".table").get(0);  9

//            String downUrl = String.format("https://www.javbus.com/ajax/uncledatoolsbyajax.php?gid=%s&lang=zh&img=%s&uc=%s&floor=%s",);
    }


    /**
     * 获取影片详情
     * 包含下载地址
     * @param movieBox
     */
    private void getDetailDownUrl(Element movieBox,List<JavBusDowns> javBusList) {
        String contextSub = SeleniumUtils.getHtml(movieBox.attr("href"));
//            String contextSub = ApacheHttpClientUtils.sendGet(movieBox.attr("href"));
        Document docSub = Jsoup.parse(contextSub);
        Elements genre = docSub.select(".row").select(".genre");
        //分类   <span class="genre"><a href="https://www.javbus.com/genre/e">巨乳</a></span>
        String genreStr = genre.select("a").attr("href");
//        log.info(genre.select("a").attr("href") );
        //分类名称
        log.info(genre.select("a").text());
        String genreName = genre.select("a").text();

        //<span class="genre" onmouseover="hoverdiv(event,'star_q8t')" onmouseout="hoverdiv(event,'star_q8t')"> <a href="https://www.javbus.com/star/q8t">河北はるな</a> </span>
//            log.info(genre.select("a").select("font").select("font").text());
        //明星 链接
//        log.info(genre.last().select("a").attr("href"));
        String starUrl = genre.last().select("a").attr("href");
        //明星 名称
//        log.info(genre.last().select("a").text());
        String starName = genre.last().select("a").text();


//            String downParam = docSub.select("script").get(8).html();
//            String[] downParams = downParam.replaceAll("var","").replaceAll(" ","").replaceAll("\n","").replaceAll("\t","").replaceAll("'","").split(";");
//            log.info(downParam);
//            String downUrl = String.format("https://www.javbus.com/ajax/uncledatoolsbyajax.php?%s&lang=zh&img=%s&uc=%s&floor=%s",downParams[0],downParams[1],downParams[2]);

        //https://www.javbus.com/ajax/uncledatoolsbyajax.php?gid=44473963134&lang=zh&img=https://pics.javbus.com/cover/7vz6_b.jpg&uc=0&floor=688
//            Element table = docSub.select("tr[onmouseover]").select(".table").get(0);  9

//            String downUrl = String.format("https://www.javbus.com/ajax/uncledatoolsbyajax.php?gid=%s&lang=zh&img=%s&uc=%s&floor=%s",);

        JavBusDowns javBusDetail = javBusList.get(0);
        Elements trs = docSub.select("#magnet-table").select("tr");
        for (Element tr:trs
        ) {

            JavBusDowns javBus = new JavBusDowns();
            BeanUtils.copyProperties(javBusDetail,javBus);
            javBus.setGenre(genreStr);
            javBus.setGenreName(genreName);
            javBus.setStarName(starName);
            javBus.setStarUrl(starUrl);
            Elements tds = tr.select("td").get(0).select("a");
            if (tds == null || tds.size() < 1 || StringUtils.isEmpty(tds.get(0).attr("href"))) {
                continue;
            }
            //下载地址
//            log.info(tds.get(0).attr("href"));
            javBus.setDownUrl(tds.get(0).attr("href"));
            StringBuffer tags = new StringBuffer("  ");
            for (int j = 1; j < tds.size(); j++
            ) {
                //标签
//                log.info(tds.get(j).attr("title"));
                //影片标签
                tags.append(tds.get(j).attr("title") + ",");
            }
            javBus.setDownTags(tags.substring(0,tags.length()-1));
            //大小
//            log.info(tr.select("td").get(1).select("a").text());
            javBus.setFileSize(tr.select("td").get(1).select("a").text());
            //时间，有效期
//            log.info(tr.select("td").get(2).select("a").text());
            javBus.setFileEndTime(tr.select("td").get(2).select("a").text());
            javBusList.add(javBus);
        }
    }

}
