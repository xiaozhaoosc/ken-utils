package com.xxl.job.executor.service.impl;

import com.xxl.job.executor.dao.PronDao;
import com.xxl.job.executor.service.JavServer;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.kenz.utils.ApacheHttpClientUtils;

@Service
@Slf4j
public class JavServerImpl implements JavServer {

    @Autowired
    private PronDao pronDao;

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

        int i = 0;
        for (Element masonry:masonrys
             ) {
            i++;
            if(i > 1){
                return;
            }
            Element movieBox = masonry.selectFirst(".movie-box");
            log.info(movieBox.attr("href") );
            log.info(movieBox.select(".photo-frame").select("img").attr("src") );
            log.info(movieBox.select(".photo-frame").select("img").attr("title") );

            Elements btnTags = movieBox.select(".photo-info").select(".btn");
            for (Element btn:btnTags
                 ) {
                log.info(btn.attr("title") );
            }

            String contextSub = ApacheHttpClientUtils.sendGet(movieBox.attr("href"));





            Document docSub = Jsoup.parse(contextSub);
            Elements genre = docSub.select(".row").select(".genre");
            //分类   <span class="genre"><a href="https://www.javbus.com/genre/e">巨乳</a></span>
            log.info(genre.select("a").attr("href") );
            log.info(genre.select("a").text());

            //<span class="genre" onmouseover="hoverdiv(event,'star_q8t')" onmouseout="hoverdiv(event,'star_q8t')"> <a href="https://www.javbus.com/star/q8t">河北はるな</a> </span>
//            log.info(genre.select("a").select("font").select("font").text());
            log.info(genre.last().select("a").attr("href"));
            log.info(genre.last().select("a").text());


            String downParam = docSub.select("script").get(8).html();
            String[] downParams = downParam.replaceAll("var","").replaceAll(" ","").replaceAll("\n","").replaceAll("\t","").replaceAll("'","").split(";");
            log.info(downParam);
            String downUrl = String.format("https://www.javbus.com/ajax/uncledatoolsbyajax.php?%s&lang=zh&img=%s&uc=%s&floor=%s",downParams[0],downParams[1],downParams[2]);

            //https://www.javbus.com/ajax/uncledatoolsbyajax.php?gid=44473963134&lang=zh&img=https://pics.javbus.com/cover/7vz6_b.jpg&uc=0&floor=688
//            Element table = docSub.select("tr[onmouseover]").select(".table").get(0);  9

//            String downUrl = String.format("https://www.javbus.com/ajax/uncledatoolsbyajax.php?gid=%s&lang=zh&img=%s&uc=%s&floor=%s",);
//            Elements trs = docSub.select("tr a");
//            for (Element tr:trs
//            ) {
//                log.info(tr.select("td").get(0).select("a").attr("href") );
//                log.info(tr.select("td").get(0).select("a").attr("title") );
//
//                log.info(tr.select("td").get(1).select("a").select("font").text() );
//                log.info(tr.select("td").get(2).select("a").select("font").select("font").text());
//            }
        }
//        String html = ApacheHttpClientUtils.sendGet(url, headParam);
//        System.out.println(html);

    }

}
