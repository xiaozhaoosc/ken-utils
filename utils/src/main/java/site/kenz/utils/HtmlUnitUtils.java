package site.kenz.utils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HtmlUnitUtils {

    public static void main(String[] args) throws Exception {
        System.setProperty("socksProxyHost", "127.0.0.1");
        System.setProperty("socksProxyPort", "10808");
        String url="https://www.javbus.com/STARS-260";
        String htmlStr = getHtml(url);
        System.out.println(htmlStr);
    }

    public static String getHtml(String url){
        try {
            WebClient wc = new WebClient(BrowserVersion.CHROME);
            // 启用JS解释器，默认为true
            wc.getOptions().setJavaScriptEnabled(true);
            // 禁用css支持
            wc.getOptions().setCssEnabled(false);
            // js运行错误时，是否抛出异常
            wc.getOptions().setThrowExceptionOnScriptError(false);
            // 状态码错误时，是否抛出异常
            wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
            // 设置连接超时时间 ，这里是5S。如果为0，则无限期等待
            wc.getOptions().setTimeout(50000);
            // 是否允许使用ActiveX
            wc.getOptions().setActiveXNative(true);
            // 等待js时间
            wc.waitForBackgroundJavaScript(20 * 1000);
            // 设置Ajax异步处理控制器即启用Ajax支持
            wc.setAjaxController(new NicelyResynchronizingAjaxController());
            // 不跟踪抓取
            wc.getOptions().setDoNotTrackEnabled(false);
            HtmlPage page = wc.getPage(url);
            // 以xml的形式获取响应文本
            String pageXml = page.asXml();
            return pageXml;
        } catch (Exception e) {
            return null;
        }
    }
}
