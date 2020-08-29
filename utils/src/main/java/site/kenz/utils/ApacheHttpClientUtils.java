package site.kenz.utils;

import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApacheHttpClientUtils {
    final static CloseableHttpClient client = HttpClients.createDefault();

    // fluent 链式调用
    public static String sendGet(String url) throws Exception {
        http://127.0.0.1:10810/pac/?t=170124
        System.setProperty("socksProxyHost", "127.0.0.1");
        System.setProperty("socksProxyPort", "10808");
//        executor.execute(Request.Get(url))
//                .returnContent().asString();
        return Request.Get(url)
                .connectTimeout(1000)
                .socketTimeout(1000)
                .execute().returnContent().asString();
    }

    // fluent 链式调用
    public static String sendGet(String url, Map<String, String> headParams) throws Exception {
        http://127.0.0.1:10810/pac/?t=170124
        System.setProperty("socksProxyHost", "127.0.0.1");
        System.setProperty("socksProxyPort", "10808");
//        executor.execute(Request.Get(url))
//                .returnContent().asString();
        Request request = Request.Get(url)
                .connectTimeout(1000)
                .socketTimeout(1000);
        if (headParams != null) {
            for (Map.Entry <String, String> entry : headParams.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return request.execute().returnContent().asString();
    }


    /**
     * fluent 链式调用
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String sendPostForm(String url, Map<String, String> params, Map<String, String> headParams) throws Exception {
        Request request = Request.Post(url)
                .connectTimeout(1000)
                .socketTimeout(1000);
        // set header
        if (headParams != null) {
            for (Map.Entry <String, String> entry : headParams.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // set params
        if (params != null) {
            List <NameValuePair> nameValuePairList = new ArrayList <NameValuePair>();
            for (Map.Entry <String, String> entry : params.entrySet()) {
                nameValuePairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity bodyEntity = new UrlEncodedFormEntity(nameValuePairList, "UTF-8");
            System.out.println("body:" + IOUtils.toString(bodyEntity.getContent()));
            request.body(new UrlEncodedFormEntity(nameValuePairList));
        }
        return request.execute().returnContent().asString();
    }

    /**
     * fluent 链式调用
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String sendPostFormV2(String url, Map<String, String> params, Map<String, String> headParams) throws Exception {
        HttpPost request = new HttpPost(url);
        // set header
        if (headParams != null) {
            for (Map.Entry <String, String> entry : headParams.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
        // set params
        if (params != null) {
            List <NameValuePair> nameValuePairList = new ArrayList <NameValuePair>();
            for (Map.Entry <String, String> entry : params.entrySet()) {
                nameValuePairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
//            UrlEncodedFormEntity bodyEntity = new UrlEncodedFormEntity(nameValuePairList, "UTF-8");
            //System.out.println("body:" + IOUtils.toString(bodyEntity.getContent()));
            request.setEntity(new UrlEncodedFormEntity(nameValuePairList));
        }
        // send request
        CloseableHttpResponse response = client.execute(request);


        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= 300) {
            throw new HttpResponseException(
                    statusLine.getStatusCode(),
                    statusLine.getReasonPhrase());
        }
        if (entity == null) {
            throw new ClientProtocolException("Response contains no content");
        }
//                DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        try {
//                    DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            ContentType contentType = ContentType.getOrDefault(entity);
            if (!contentType.equals(ContentType.APPLICATION_XML)) {
                throw new ClientProtocolException("Unexpected content type:" +
                        contentType);
            }
//                    String charset = contentType.getCharset().displayName();
//                    if (charset == null) {
//                        charset = "UTF-8";
//                    }

            String ret = readResponseContent(entity.getContent());
            response.close();
            return ret;
        } catch (ParserConfigurationException ex) {
            throw new IllegalStateException(ex);
        } catch (SAXException ex) {
            throw new ClientProtocolException("Malformed XML document", ex);
        } catch (Exception exception) {
            throw new ClientProtocolException("readResponseContent fail", exception);
        }
        // read rsp code
//        System.out.println("rsp code:" + response.getStatusLine().getStatusCode());
        // return content
    }
    /**
     * fluent 链式调用
     * @param url
     * @param json
     * @return
     * @throws Exception
     */
    public static String sendPost(String url,String json) throws Exception {
        Executor executor = Executor.newInstance();
                //将身份验证细节缓存并重新用于后续请求
//                .auth(new HttpHost("somehost"), "username", "password")
//        executor.auth(new HttpHost("127.0.0.1", 10808, "SOCKS5"), "", "");
//                .authPreemptive(new HttpHost("myproxy", 8080));
        // SOCKS 代理，支持 HTTP 和 HTTPS 请求
        // 注意：如果设置了 SOCKS 代理就不要设 HTTP/HTTPS 代理
//        System.setProperty("proxySet", "true");
//        System.setProperty("java.net.useSystemProxies", "true");
        System.setProperty("socksProxyHost", "127.0.0.1");
        System.setProperty("socksProxyPort", "10808");
        String response = executor.execute(Request.Post(url)
                .useExpectContinue()
//                .viaProxy(new HttpHost("127.0.0.1", 10808, "SOCKS5"))
                .bodyString(json, ContentType.APPLICATION_JSON))
                .returnContent().asString();
        return response;
    }


    /**
     * fluent 链式调用
     * 使用ResponseHandler进行HTTP响应处理，以避免在内存中缓冲的内容丢失
     * @param url
     * @param json
     * @return
     * @throws Exception
     */
    public static String sendPostV2(String url,String json) throws Exception {
        String response = Request.Post(url)
                .useExpectContinue()
                .bodyString(json, ContentType.APPLICATION_JSON).execute().handleResponse(
                        new ResponseHandler<String>() {
            public String handleResponse(final HttpResponse response) throws IOException {
                StatusLine statusLine = response.getStatusLine();
                HttpEntity entity = response.getEntity();
                if (statusLine.getStatusCode() >= 300) {
                    throw new HttpResponseException(
                            statusLine.getStatusCode(),
                            statusLine.getReasonPhrase());
                }
                if (entity == null) {
                    throw new ClientProtocolException("Response contains no content");
                }
//                DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
                try {
//                    DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
                    ContentType contentType = ContentType.getOrDefault(entity);
                    if (!contentType.equals(ContentType.APPLICATION_XML)) {
                        throw new ClientProtocolException("Unexpected content type:" +
                                contentType);
                    }
//                    String charset = contentType.getCharset().displayName();
//                    if (charset == null) {
//                        charset = "UTF-8";
//                    }

                    String ret = readResponseContent(entity.getContent());
                    return ret;
                } catch (ParserConfigurationException ex) {
                    throw new IllegalStateException(ex);
                } catch (SAXException ex) {
                    throw new ClientProtocolException("Malformed XML document", ex);
                } catch (Exception exception) {
                    throw new ClientProtocolException("readResponseContent fail", exception);
                }
            }

        });
        return response;
    }

    private static String readResponseContent(InputStream inputStream) throws Exception {
        if (inputStream == null) {
            return "";
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[512];
        int len;
        while (inputStream.available() > 0) {
            len = inputStream.read(buf);
            out.write(buf, 0, len);
        }

        return out.toString();
    }

    public static void main(String[] args) throws Exception {
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


        getVideo(headParam);
    }

    private static void getVideo(Map<String, String> headParam) {
        String indexUrl = "https://www.pornhub.com";
        String searchUrl = "https://cn.pornhub.com/video?c=99";
        String searchHtml = null;
        try {
            searchHtml = ApacheHttpClientUtils.sendGet(searchUrl,headParam);
        } catch (Exception e) {
            e.printStackTrace();
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
//                System.out.println(viewsNum + ":" + viewsNum + ":" + viewsNum );
                viewsNum = viewsNum.replaceAll(",", "");
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
                try {
                    detailHtml = ApacheHttpClientUtils.sendGet(indexUrl + openUrl,headParam);
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
//                    https://cd.phncdn.com/videos/202006/01/319733721/1080P_4000K_319733721.mp4?13htYNFCMIJ0Gt64iI0Jc2YHj3MVyEb2Bkg7ws9SuhABs5PIpEE5GCC0DgxXmcE8EWEakxkozKVaS3TEr5m4B-k-yCMlkprwoOtoKBPA4BIqCZsEtCilxv1KOhoSOQZ2Oy6Xfr5kKapMVEr0tt0NV213ShN0QJF7pljerDIWQLpQMugk0GNQSGQ9BcdVp4Bj26ItGG-doJrjh3kGy9wr9yJPD86rxMZDtrtHkSDGkIuh7zDrFJ8o
                    String hrefUrl = tabs.get(1).attr("href");
                    System.out.println(hrefUrl);


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

    private static void postIndex() throws Exception {
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
