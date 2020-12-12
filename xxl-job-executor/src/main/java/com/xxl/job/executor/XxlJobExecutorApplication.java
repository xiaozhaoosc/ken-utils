package com.xxl.job.executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xuxueli 2018-10-28 00:38:13
 */
@SpringBootApplication
public class XxlJobExecutorApplication {

	public static void main(String[] args) {

        System.setProperty("socksProxyHost", "127.0.0.1");
        System.setProperty("socksProxyPort", "10808");
        SpringApplication.run(XxlJobExecutorApplication.class, args);
	}

}