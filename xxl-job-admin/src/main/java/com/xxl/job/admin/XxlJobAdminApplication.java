package com.xxl.job.admin;

import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author xuxueli 2018-10-28 00:38:13
 */
@SpringBootApplication
@Slf4j
public class XxlJobAdminApplication {

	private static Logger logger = LoggerFactory.getLogger(XxlJobAdminApplication.class);
	public static void main(String[] args) {

		SpringApplication.run(XxlJobAdminApplication.class, args);

//		ConfigurableApplicationContext application = SpringApplication.run(XxlJobAdminApplication.class, args);
//		Environment env = application.getEnvironment();
//		String ip = null;
//		try {
//			ip = InetAddress.getLocalHost().getHostAddress();
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
//		String port = env.getProperty("server.port");
//		String path = env.getProperty("server.servlet.context-path");
//
//		logger.info("\n----------------------------------------------------------\n\t" +
//				"Application Jeecg-Boot is running! Access URLs:\n\t" +
//				"Local: \t\thttp://localhost:" + port  + "/xxl-job-admin\n\t" +
//				"External: \thttp://" + ip + ":" + port + "/xxl-job-admin\n\t" +
//               /* "swagger-ui: \thttp://" + ip + ":" + port + path + "/swagger-ui.html\n\t" +
//                "Doc: \t\thttp://" + ip + ":" + port + path + "/doc.html\n" +*/
//				"----------------------------------------------------------");
//		logger.info("启动成功  当前版本编译时间  =====》 ");
//		SpringApplication.run(XxlJobAdminApplication.class, args);
	}

}