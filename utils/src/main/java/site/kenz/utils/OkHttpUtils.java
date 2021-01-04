package site.kenz.utils;

import com.google.gson.JsonObject;
import io.netty.channel.unix.DomainSocketAddress;
import okhttp3.*;
import org.apache.http.util.CharsetUtils;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
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



    public static String get() throws Exception{
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, InetSocketAddress.createUnresolved(
                "127.0.0.1", 10808));

        SocketAddress domainSocketAddress = new DomainSocketAddress("http://127.0.0.1:10810/pac/?t=164420");

        Proxy p = new Proxy(Proxy.Type.SOCKS, domainSocketAddress);
        Socket s = new Socket(p);
        InetSocketAddress addr = InetSocketAddress.createUnresolved("127.0.0.1", 10808);
        s.connect(addr);

        OkHttpClient clientProxy = new OkHttpClient.Builder()
                .proxy(p).build();

        String url2 = "xxx/sendPhoto";
        JsonObject jsonObject0 = new JsonObject();
        //https://t.me/meitujianshang
        jsonObject0.addProperty("chat_id","@meitujianshang");
//        jsonObject0.addProperty("chat_id","-1001285034592");
        jsonObject0.addProperty("photo","https://wxt.sinaimg.cn/mw1024/9d52c073gw1eykea8tbjbj20g40len1j.jpg");

        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , jsonObject0.toString());
        Request request = new Request.Builder().url(url2).post(requestBody).build();
        String response = clientProxy.newCall(request).execute().body().string();
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {

        String sd = "var gid = 44473901038;\n" +
                "\tvar uc = 0;\n" +
                "\tvar img = 'https://pics.javbus.com/cover/7vz6_b.jpg';";
        String[] sdf = sd.replaceAll("var","").replaceAll(" ","").replaceAll("\n","").replaceAll("\t","").replaceAll("'","").split(";");
        for (String s:
        sdf) {
            System.out.println("" + s.replaceAll("var",""));
        }


//        OkHttpUtils okHttpDemo = new OkHttpUtils();
//        String url = "https://httpbin.org/post";
//        Map<String, String>  params = new HashMap<String, String>();
//        params.put("foo", "bar中文");
//        String rsp = okHttpDemo.sendPostForm(url, params);
//        System.out.println("http post rsp:" + rsp);
//
//        url = "https://httpbin.org/get";
//        System.out.println("http get rsp:" + okHttpDemo.sendGet(url));
//        get();
    }

}
