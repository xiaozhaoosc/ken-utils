package com.xxl.job.executor.service.impl;

import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.executor.dao.PronDao;
import com.xxl.job.executor.model.Pron;
import com.xxl.job.executor.service.PronVideoServer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.kenz.utils.ApacheHttpClientUtils;
import site.kenz.utils.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class PronVideoServerImpl  implements PronVideoServer {

    @Autowired
    private PronDao pronDao;
    /**
     * 输入cookie
     *
     * @param params
     */
    @Override
    public void getVideosByCookie(String[] params) {
        getVideo(params);
    }

    /**
     * 输入密码的Star
     *
     * @param params
     */
    @Override
    public void getVideosByCookieStar(String[] params) {
        getVideoStar(params);
    }

    /**
     * 自动登陆的
     * 前两个参数为账号和密码
     *
     * @param params
     */
    @Override
    public void getVideos(String[] params) {

    }

    private Map getHead(String cookie) {
        //请求首页
//        postIndex();

//        String telBot = "xxx/sendVideo";
////        String telBot = "xxx/sendMessage";
//        JsonObject jsonObject = new JsonObject();
////        jsonObject.addProperty("chat_id","@ken2zhao");
//        jsonObject.addProperty("chat_id","@meitujianshang");
//        jsonObject.addProperty("support_streaming","True");
//        jsonObject.addProperty("text","https://cd.phncdn.com/videos/202007/31/337921151/240P_400K_337921151.mp4");
//        try {
//            sendPost(telBot, jsonObject.toString());
//        } catch (Exception e) {
//            System.out.println("e：" + e.getMessage());
//            e.printStackTrace();
//        }
//        String cookie = "ua=fa951fa6cd6a040efa24debcc1e745cd; platform_cookie_reset=pc; platform=pc; bs=ngduxz9qyp0exphobipkum7gkgioyikl; ss=366429587298295058; fg_9d12f2b2865de2f8c67706feaa332230=33622.100000; _ga=GA1.2.743455686.1598673960; _gid=GA1.2.1094844638.1598673960; il=v16xxqZypvLcHbaDKcuLRivBRSJGtxHxf_QetsORBlLTwxNjA2NDUwMDU0c2VuekgydVIycWJYd3lHTWdrZkJKc2E2WUx2VnN4SG0zVmkweEFzMw..; expiredEnterModalShown=1";
        Map<String, String> headParam = new HashMap<String, String>();
//        headParam.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headParam.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        headParam.put("Accept-Encoding", "gzip, deflate, br");
        headParam.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        headParam.put("Cache-Control", "no-cache");
        headParam.put("Connection", "keep-alive");
        headParam.put("Cookie", cookie);
        headParam.put("Host", "cn.pornhub.com");
        headParam.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36 Edg/85.0.564.41");
        return headParam;

//        getVideo(headParam);
    }

    private void getVideo(String[] params) {
        Map<String, String> headParam = getHead(params[0]);
        String indexUrl = "https://www.pornhub.com";
        if (params.length > 1) {
            for (int l = 1; l < params.length; l++) {
                String searchUrl = params[l];
                if (searchUrl.indexOf("pornhub.com") < 0) {
                    searchUrl = indexUrl + searchUrl;
                }
                String searchHtml = null;
                try {
                    searchHtml = ApacheHttpClientUtils.sendGet(searchUrl,headParam);
                } catch (Exception e) {
                    continue;
                }
                Document searchDoc = Jsoup.parse(searchHtml);
                Elements wrappers = searchDoc.select(".thumbnail-info-wrapper");
                if (wrappers != null && wrappers.size() > 0) {
                    for (int i = 0; i < wrappers.size(); i++) {
                        Element element = wrappers.get(i);
                        //评分
                        String valNum = element.select(".value").text();
                        //观看次数
                        String viewsNum = element.select(".views").select("var").text();
                        String viewsStr = viewsNum;
//                System.out.println(viewsNum + ":" + viewsNum + ":" + viewsNum );
                        viewsNum = viewsNum.replaceAll(",", "");
                        if (viewsNum.length() < 1) {
                            continue;
                        }
                        String lastStr = viewsNum.substring(viewsNum.length()-1);
                        int beishu = 1;
                        if ("K".equals(lastStr)) {
                            beishu = 1000;
                            viewsNum = viewsNum.substring(0, viewsNum.length()-1);
                        }else if("M".equals(lastStr)){
                            beishu = 10000;
                            viewsNum = viewsNum.substring(0, viewsNum.length()-1);
                        }
//                b > 78 && c > 300000
                        String openUrl = element.select("a").attr("href");
                        valNum = valNum.replace("%", "");
                        int val = 30;
                        int views = 30;

                        try {
                            val = Integer.valueOf(valNum);
                            views = new BigDecimal(viewsNum).multiply(new BigDecimal(beishu)).intValue();
//                    System.out.println(val + ":" + views + ":" + viewsNum );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                System.out.println("indexUrl + openUrl：" + indexUrl + openUrl);

                        String detailHtml = null;
                        String parentUrl = indexUrl + openUrl;
                        try {
                            detailHtml = ApacheHttpClientUtils.sendGet(parentUrl,headParam);
                        } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println("error" + detailHtml);
                        }
                        if (detailHtml == null) {
                            continue;
                        }
                        Document detailDoc = Jsoup.parse(detailHtml);
                        Elements tabs = detailDoc.select(".video-actions-container").select(".video-actions-tabs").select(".video-action-tab").select("a.downloadBtn");
                        if (tabs != null && tabs.size() > 0) {
                            if (!(val > 80 || views > 30000)) {
                                continue;
                            }
                            Element ele = tabs.get(0);
                            String downUrl = ele.attr("href");
                            if (StringUtils.isEmpty(downUrl)) {
                                ele = tabs.get(1);
                                downUrl = ele.attr("href");
                            }
//                            String title = detailDoc.select(".video-wrapper").select(".title-container").select(".title").select(".inlineFree").text();
                            String title = detailDoc.select(".inlineFree").text();
                            XxlJobLogger.log(downUrl);
                            Pron pron = new Pron();
                            pron.setParentUrl(parentUrl);
                            pron.setTitleName(detailDoc.title());
                            pron.setTitle(title);
                            pron.setValid(val);
                            pron.setViewsNum(views);
                            pron.setViewsNumStr(viewsStr);
                            pron.setUrl(downUrl);
                            pronDao.save(pron);

//                    String telBot = "xxx/sendVideo";
//                    JsonObject jsonObject = new JsonObject();
//                    jsonObject.addProperty("chat_id","@ken2zhao");
//                    jsonObject.addProperty("video",hrefUrl);
//                    try {
//                        sendPost(telBot, jsonObject.toString());
//                    } catch (Exception e) {
//                        System.out.println("e：" + e.getMessage());
//                    }

//                    String telBot = "xxx/sendMessage";
//                    JsonObject jsonObject = new JsonObject();
//                    jsonObject.addProperty("chat_id","@ken2zhao");
//                    jsonObject.addProperty("text",hrefUrl);
//                    try {
//                        sendPost(telBot, jsonObject.toString());
//                    } catch (Exception e) {
//                        System.out.println("e：" + e.getMessage());
//                    }
                        }
//                System.out.println("valNum：" + valNum);
                    }
                }
            }
        }
    }

    private void getVideoStar(String[] params) {
        Map<String, String> headParam = getHead(params[0]);
        if (params.length > 1) {
            for (int l = 1; l < params.length; l++) {
                String searchUrl = params[l];
                String searchHtml = null;
                try {
                    searchHtml = ApacheHttpClientUtils.sendGet(searchUrl,headParam);
                } catch (Exception e) {
                    continue;
                }
                Document searchDoc = Jsoup.parse(searchHtml);
                Elements wrappers = searchDoc.select(".thumbnail-info-wrapper");
                if (wrappers != null && wrappers.size() > 0) {
                    for (int i = 0; i < wrappers.size(); i++) {
                        Element element = wrappers.get(i);
                        //评分
                        String valNum = element.select(".value").text();
                        //观看次数
                        String viewsNum = element.select(".views").select("var").text();
                        String viewsStr = viewsNum;
//                System.out.println(viewsNum + ":" + viewsNum + ":" + viewsNum );
                        viewsNum = viewsNum.replaceAll(",", "");
                        if (viewsNum.length() < 1) {
                            continue;
                        }
                        String lastStr = viewsNum.substring(viewsNum.length()-1);
                        int beishu = 1;
                        if ("K".equals(lastStr)) {
                            beishu = 1000;
                            viewsNum = viewsNum.substring(0, viewsNum.length()-1);
                        }else if("M".equals(lastStr)){
                            beishu = 10000;
                            viewsNum = viewsNum.substring(0, viewsNum.length()-1);
                        }
//                b > 78 && c > 300000
                        String openUrl = element.select("a").attr("href");
                        valNum = valNum.replace("%", "");
                        int val = 30;
                        int views = 30;

                        try {
                            val = Integer.valueOf(valNum);
                            views = new BigDecimal(viewsNum).multiply(new BigDecimal(beishu)).intValue();
//                    System.out.println(val + ":" + views + ":" + viewsNum );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        getVideo(new String[]{params[0],openUrl});
//                        try {
//                            detailHtml = ApacheHttpClientUtils.sendGet(parentUrl,headParam);
//                        } catch (Exception e) {
////                    e.printStackTrace();
////                    System.out.println("error" + detailHtml);
//                        }
//                        if (detailHtml == null) {
//                            continue;
//                        }
                    }
                }
            }
        }
    }


    private void postIndex() throws Exception {
        String url = "https://www.pornhub.com";
//        System.out.println("http get rsp:" + ApacheHttpClientUtils.sendGet(url));
        String context = ApacheHttpClientUtils.sendGet(url);
        System.setProperty("socksProxyHost", "127.0.0.1");
        System.setProperty("socksProxyPort", "10808");
        Document doc = Jsoup.parse(context);
//        Document doc = Jsoup.connect(url).get();
//        System.out.println(doc.title());
        Elements loginFormModal = doc.select(".js-loginFormModal");
        String redirect = loginFormModal.select(".js-redirect").attr("value");
        String token = loginFormModal.select("[name='token']").attr("value");
        String rememberMe = loginFormModal.select("[name='remember_me']").attr("value");
        String from = loginFormModal.select("[name='from']").attr("value");

        //登陆页面
        String loginUrl = "https://www.pornhub.com/login";
        Map<String, String> headParam = new HashMap<String, String>();
//        headParam.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headParam.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        headParam.put("Accept-Encoding", "gzip, deflate, br");
        headParam.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        headParam.put("Cache-Control", "no-cache");
        headParam.put("Connection", "keep-alive");
        headParam.put("Cookie", "ua=fa951fa6cd6a040efa24debcc1e745cd; platform_cookie_reset=pc; platform=pc; bs=ngduxz9qyp0exphobipkum7gkgioyikl; ss=366429587298295058; fg_9d12f2b2865de2f8c67706feaa332230=33622.100000; _ga=GA1.2.743455686.1598673960; _gid=GA1.2.1094844638.1598673960; il=v16xxqZypvLcHbaDKcuLRivBRSJGtxHxf_QetsORBlLTwxNjA2NDUwMDU0c2VuekgydVIycWJYd3lHTWdrZkJKc2E2WUx2VnN4SG0zVmkweEFzMw..; expiredEnterModalShown=1");
        headParam.put("Host", "cn.pornhub.com");
        headParam.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36 Edg/85.0.564.41");

        Map<String, String> param = new HashMap<String, String>();
        param.put("redirect", redirect);
        param.put("token", token);
        param.put("remember_me", rememberMe);
        param.put("from", from);
        param.put("username", "kenzhao");
        param.put("password", "a123456");
        String html = ApacheHttpClientUtils.sendPostForm(loginUrl, headParam, param);
//        String html = ApacheHttpClientUtils.sendGet(url, headParam);
//        System.out.println(html);

        //登陆中间页面
        Document docLogin = Jsoup.parse(html);
//        Document doc = Jsoup.connect(url).get();
//        System.out.println(doc.title());
        Elements loginFormModal2 = docLogin.select(".js-loginFormModal");
        String redirect2 = loginFormModal2.select(".js-redirect").attr("value");
        String token2 = loginFormModal2.select("[name='token']").attr("value");
        String rememberMe2 = loginFormModal2.select("[name='remember_me']").attr("value");
        String from2 = loginFormModal2.select("[name='from']").attr("value");
        param = new HashMap<String, String>();

        param.put("redirect", redirect2);
        param.put("token", token2);
        param.put("remember_me", rememberMe2);
        param.put("from", from2);
        param.put("username", "kenzhao");
        param.put("password", "a123456");
        String authenticateUrl = "https://www.pornhub.com/front/authenticate";
        String authenticateHtml = ApacheHttpClientUtils.sendPostForm(authenticateUrl, headParam, param);
    }
}
