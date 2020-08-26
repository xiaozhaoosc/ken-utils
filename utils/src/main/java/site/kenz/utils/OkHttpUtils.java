package site.kenz.utils;

import okhttp3.*;
import org.apache.http.util.CharsetUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

public class OkHttpUtils {
    OkHttpClient client = new OkHttpClient();

    private String sendPostForm(String url, final Map<String, String>  params) throws Exception {
        FormBody.Builder builder = new FormBody.Builder(CharsetUtils.get("UTF-8"));
        if (params != null) {
            for (Map.Entry<String, String>  entry: params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return client.newCall(request).execute().body().string();

    }

    private String sendGet(String url) throws Exception {
        Request request = new Request.Builder().url(url).build();
        return  client.newCall(request).execute().body().string();
    }

    public static void main(String[] args) throws Exception {
        OkHttpUtils okHttpDemo = new OkHttpUtils();
        String url = "https://httpbin.org/post";
        Map<String, String>  params = new HashMap<String, String>();
        params.put("foo", "bar中文");
        String rsp = okHttpDemo.sendPostForm(url, params);
        System.out.println("http post rsp:" + rsp);

        url = "https://httpbin.org/get";
        System.out.println("http get rsp:" + okHttpDemo.sendGet(url));
    }

//    public String get(){
//        Proxy proxy = new Proxy(Proxy.Type.SOCKS, InetSocketAddress.createUnresolved(
//                "socks5host", 80));
//        OkHttpClient client = new OkHttpClient.Builder()
//                .proxy(proxy).authenticator(new Authenticator() {
//                    @Override
//                    public Request authenticate(Route route, Response response) throws IOException {
//                        if (HttpUtils.responseCount(response) >= 3) {
//                            return null;
//                        }
//                        String credential = Credentials.basic("user", "psw");
//                        if (credential.equals(response.request().header("Authorization"))) {
//                            return null; // If we already failed with these credentials, don't retry.
//                        }
//                        return response.request().newBuilder().header("Authorization", credential).build();
//                    }
//                }).build();
//
//
//        Request request = new Request.Builder().url("http://google.com").get().build();
//        Response response = client.newCall(request).execute();
//        System.out.println(response.body().string());
//    }
}
