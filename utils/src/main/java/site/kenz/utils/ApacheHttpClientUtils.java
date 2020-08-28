package site.kenz.utils;

import com.google.gson.JsonObject;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApacheHttpClientUtils {
    final static CloseableHttpClient client = HttpClients.createDefault();

    // 常规调用
    private String sendPostForm(String url, Map <String, String> params) throws Exception {
        HttpPost request = new HttpPost(url);

        // set header
        request.setHeader("X-Http-Demo", ApacheHttpClientUtils.class.getSimpleName());

        // set params
        if (params != null) {
            List <NameValuePair> nameValuePairList = new ArrayList <NameValuePair>();
            for (Map.Entry <String, String> entry : params.entrySet()) {
                nameValuePairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity bodyEntity = new UrlEncodedFormEntity(nameValuePairList, "UTF-8");
            //System.out.println("body:" + IOUtils.toString(bodyEntity.getContent()));
            request.setEntity(new UrlEncodedFormEntity(nameValuePairList));
        }

        // send request
        CloseableHttpResponse response = client.execute(request);
        // read rsp code
        System.out.println("rsp code:" + response.getStatusLine().getStatusCode());
        // return content
        String ret = readResponseContent(response.getEntity().getContent());
        response.close();
        return ret;
    }

    // fluent 链式调用
    private String sendGet(String url) throws Exception {
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
        ApacheHttpClientUtils httpUrlConnectionDemo = new ApacheHttpClientUtils();
        String url = "xxx/getMe";
//        Map <String, String > params = new HashMap<String, String>
//        ();
//        params.put("foo", "bar中文");
//        String rsp = httpUrlConnectionDemo.sendPostForm(url, params);
//        System.out.println("http post rsp:" + rsp);
//
//        url = "https://httpbin.org/get";
        /*
        ken zhao, [25.08.20 18:01]
[转发自频道 美图鉴赏]
[ 图片 ]

GetIDs Bot, [25.08.20 18:01]
👤 You
 ├ id: 522851631
 ├ is_bot: false
 ├ first_name: ken
 ├ last_name: zhao
 ├ username: ken2zhao (https://t.me/ken2zhao)
 ├ language_code: zh-hans (-)
 └ created: ~ 3/2018 (?) (https://t.me/getidsbot?start=idhelp)

💬 Origin chat
 ├ id: -1001285034592
 ├ title: 美图鉴赏
 ├ username: meitujianshang (https://t.me/meitujianshang)
 └ type: channel

🖼 Image
 └ file_size: 86.89 kB

📃 Message
 ├ message_id: 865 (https://t.me/meitujianshang/865)
 └ forward_date: Tue, 25 Aug 2020 07:45:11 GMT
         */
        System.out.println("http get rsp:" + httpUrlConnectionDemo.sendGet(url));
        //https://api.telegram.org/bot<token>/METHOD_NAME
        String url2 = "xxx/sendPhoto";
        String url3 = "xxx/sendMediaGroup";
        JsonObject jsonObject = new JsonObject();
        //https://t.me/meitujianshang
        jsonObject.addProperty("chat_id","@meitujianshang");
//        jsonObject.addProperty("chat_id","-1001285034592");
//        jsonObject.addProperty("photo","https://wxt.sinaimg.cn/mw1024/9d52c073gw1eyh40ammcyj20k80qzgo6.jpg");

        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("type","photo");
        jsonObject1.addProperty("media","https://wxt.sinaimg.cn/mw1024/9d52c073gw1eykea8tbjbj20g40len1j.jpg");

        //{\"type\":\"photo\",\"media\":\"https://wxt.sinaimg.cn/mw1024/9d52c073gw1eyh40cugx7j20k80qz0za.jpg\"},{\"type\":\"photo\",\"media\":\"https://wxt.sinaimg.cn/mw1024/9d52c073gw1eyh40cwdplj20k80qzwk8.jpg\"},
        //
        // {\"type\":\"photo\",\"media\":\"https://wxt.sinaimg.cn/mw1024/9d52c073gw1eyh40ch5iej20k80qzq8h.jpg\"},{\"type\":\"photo\",\"media\":\"https://wxt.sinaimg.cn/mw1024/9d52c073gw1eyh40ammcyj20k80qzgo6.jpg\"}

        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("type","photo");
        jsonObject2.addProperty("media","https://wxt.sinaimg.cn/mw1024/9d52c073gw1eylhmnddcmj21f41w0kjl.jpg");

        JsonObject jsonObject3 = new JsonObject();
        jsonObject3.addProperty("type","photo");
        jsonObject3.addProperty("media","");

        JsonObject jsonObject4 = new JsonObject();
        jsonObject4.addProperty("type","photo");
        jsonObject4.addProperty("media","");

        List<JsonObject> list = new ArrayList<JsonObject>();
        list.add(jsonObject1);
        list.add(jsonObject2);
//        list.add(jsonObject3);
//        list.add(jsonObject4);

        jsonObject.addProperty("media",GsonUtil.objectToJson(list));
//        jsonObject.addProperty("caption","1");
//        jsonObject.addProperty("parse_mode","1");
//        jsonObject.addProperty("disable_notification","1");
//        jsonObject.addProperty("reply_to_message_id","1");
//        jsonObject.addProperty("reply_markup","1");
//        System.out.println("http post jsonObject:" + jsonObject.toString());
//        String response = ApacheHttpClientUtils.sendPost(url3, jsonObject.toString());
//        System.out.println("http post response:" + response);


        JsonObject jsonObject0 = new JsonObject();
        //https://t.me/meitujianshang
        jsonObject0.addProperty("chat_id","@meitujianshang");
//        jsonObject0.addProperty("chat_id","-1001285034592");
        jsonObject0.addProperty("photo","https://wxt.sinaimg.cn/mw1024/9d52c073gw1eykea8tbjbj20g40len1j.jpg");
        String response0 = ApacheHttpClientUtils.sendPost(url2, jsonObject0.toString());
        System.out.println("http post response:" + response0);
    }
}
