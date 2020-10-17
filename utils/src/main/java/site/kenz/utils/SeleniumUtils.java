package site.kenz.utils;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class SeleniumUtils {
//    private static final ChromeOptions CHROME_OPTIONS = new ChromeOptions();

    public static void main(String[] args) throws Exception {
        System.setProperty("socksProxyHost", "127.0.0.1");
        System.setProperty("socksProxyPort", "10808");
        String url="https://www.javbus.com/STARS-260";
        String htmlStr = getHtml(url);
        System.out.println(htmlStr);
    }

    public static String getHtml(String url){
        try {

//            System.setProperty("webdriver.chrome.driver","D:\\gitee\\ken-utils\\utils\\src\\main\\chromedriver83.exe");
            System.setProperty("webdriver.chrome.driver","C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");


            //设置chrome选项
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            // 避免被浏览器检测识别
//            CHROME_OPTIONS.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            //开启webDriver进程
            WebDriver webDriver = new ChromeDriver(options);
            try {
                webDriver.get(url);
                long waitTime = Double.valueOf(Math.max(3, Math.random() * 5) * 1000).longValue();
                TimeUnit.MILLISECONDS.sleep(waitTime);
                long timeout = 300000;
                // 循环下拉，直到全部加载完成或者超时
//                new Actions(webDriver).sendKeys(Keys.END).perform();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String pageXml = webDriver.getPageSource();
            //System.out.println(trList);
            webDriver.close();
            webDriver.quit();
            // 以xml的形式获取响应文本
            return pageXml;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
