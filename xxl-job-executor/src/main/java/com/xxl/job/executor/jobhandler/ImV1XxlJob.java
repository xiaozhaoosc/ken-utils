package com.xxl.job.executor.jobhandler;

import com.google.common.collect.Lists;
import com.jcraft.jsch.Session;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import site.kenz.utils.StringUtils;

import java.util.List;

/**
 * 推送telegrame（Bean模式）
 *
 */
@Component
public class ImV1XxlJob {
    private static Logger logger = LoggerFactory.getLogger(ImXxlJob.class);

    /**
     * 输入Cookic
     * 第1位为密码，,分隔后面的为解析地址
     */
    @XxlJob("imIpV1")
    public ReturnT<String> imIp(String param) throws Exception {
        if (StringUtils.isEmpty(param) || param.split(",").length < 2) {
            XxlJobLogger.log("参数不正确");
            return ReturnT.FAIL;
        }
        String[] params = param.split(",");
        String ip = getIp(params[0],params[1]);
        XxlJobLogger.log(ip);
        return ReturnT.SUCCESS;
    }
    /**
     * ip0,pass1,key2,英文名称
     * 没有密码，自动登陆
     * 前两个参数为账号和密码
     * ,分隔解析地址
     */
    @XxlJob("imV1")
    public ReturnT<String> im(String param) throws Exception {
        if (StringUtils.isEmpty(param) || param.split(",").length < 1) {
            XxlJobLogger.log("参数不正确");
            return ReturnT.FAIL;
        }
        String[] params = param.split(",");
//        wget https://quyangdata.oss-cn-shanghai.aliyuncs.com/fenche/init.tar
        String init = "cd /opt;wget https://quyangdata.oss-cn-shanghai.aliyuncs.com/v2/init.tar;tar -xvf init.tar";
        String jdk = "sh /opt/init/init.sh " + params[3] + ";sh /opt/init/insertJdk.sh;java -version";
        String ipStr = getIp(params[0],params[1]);
        String initDocker = "sh /opt/init/initProject.sh; sh /opt/init/initDocker.sh  " + ipStr + "  " + params[2];
        String ipaddr = "ip addr";


        XxlJobLogger.log("ipStr:" + ipStr);
        logger.info("ipStr:" + ipStr);

        XxlJobLogger.log("初始化-开始执行");
        exec(params[0],params[1],init);
        XxlJobLogger.log("初始化-结束执行");


        XxlJobLogger.log("JDK-开始执行");
        exec(params[0],params[1],jdk);
        XxlJobLogger.log("JDK-结束执行");



        XxlJobLogger.log("依赖软件安装-开始执行");
        exec(params[0],params[1],initDocker);
        XxlJobLogger.log("依赖软件安装-结束执行");
        XxlJobLogger.log("");
        return ReturnT.SUCCESS;
    }


    /**
     * ip0,pass1,域名2，名称3，key4，logo5
     * 没有密码，自动登陆
     * 前两个参数为账号和密码
     * ,分隔解析地址
     */
    @XxlJob("imStartV1")
    public ReturnT<String> imStart(String param) throws Exception {
        if (StringUtils.isEmpty(param) || param.split(",").length < 1) {
            XxlJobLogger.log("参数不正确");
            return ReturnT.FAIL;
        }
        String[] params = param.split(",");


//        String start = "source /etc/profilesh;cd /opt/spring-boot-imapi;sh stop  & ";
        String nginx = "docker restart nginx";
        String tigase = "export JAVA_HOME=/opt/java/jdk1.8.0_131;cd /opt/tigase-server-7.1.3-b4482; ./scripts/tigase.sh stop etc/tigase.conf; ./scripts/tigase.sh start etc/tigase.conf ";
        String imapi = "cd /opt/spring-boot-imapi;sh stop; nohup /opt/java/jdk1.8.0_131/bin/java -XX:+UseG1GC  -jar imapi-*.war --spring.config.location=./application.properties > /dev/null 2>&1 & ";

        String upload = "cd /opt/upload;sh stop; nohup /opt/java/jdk1.8.0_131/bin/java -XX:+UseG1GC  -jar upload-*.war --spring.config.location=application.properties > /dev/null 2>&1 & ";
        String message = "cd /opt/message-push;sh stop; nohup /opt/java/jdk1.8.0_131/bin/java -XX:+UseG1GC  -jar message-push-*.war --spring.config.location=./application.properties > /dev/null 2>&1 & ";

        String shikupush = "cd /opt/shiku-push;sh stop; nohup /opt/java/jdk1.8.0_131/bin/java -XX:+UseG1GC  -jar shiku-push-*.war --spring.config.location=./application.properties > /dev/null 2>&1 & ";
        String mpserver = "cd /opt/mp-server;sh stop; nohup /opt/java/jdk1.8.0_131/bin/java -XX:+UseG1GC  -jar mp-server-1.0.war --spring.config.location=/opt/mp-server/mpserver.properties > ./logs/log.log & ";

//        logger.info("===================================");
//        XxlJobLogger.log("项目启动-开始执行");
//        exec(params[0],params[1],tigase);
//        XxlJobLogger.log("项目启动-结束执行");
//        logger.info("===================================");

        String ipStr = getIp(params[0],params[1]);
        if (params.length > 6) {
            String start = "export JAVA_HOME=/opt/java/jdk1.8.0_131;sh /opt/init/initStartPush.sh  " + params[2] +  " " +  ipStr +  " " + params[3] +  " " + ipStr +  " " + params[4] +  " " + params[5] +  " " + params[6] +  " " + params[7];

            XxlJobLogger.log("项目启动-开始执行");
            exec(params[0],params[1],start);
            XxlJobLogger.log("项目启动-结束执行");
        }else{
            String start = "export JAVA_HOME=/opt/java/jdk1.8.0_131;sh /opt/init/initStart.sh  " + params[2] +  " " +  ipStr +  " " + params[3] +  " " + ipStr +  " " + params[4] +  " " + params[5];

            XxlJobLogger.log("项目启动-开始执行");
            exec(params[0],params[1],start);
            XxlJobLogger.log("项目启动-结束执行");
        }
        if (params[5].indexOf("jpg") > 0) {
            String logo = "sh /opt/init/logo.sh " +  params[5];
            XxlJobLogger.log("logo替换-开始执行");
            exec(params[0],params[1],logo);
            XxlJobLogger.log("logo替换-结束执行");
        }


        return ReturnT.SUCCESS;
    }

    public void init(){
        logger.info("init");
    }
    public void destroy(){
        logger.info("destory");
    }

    public void exec(String ip,String password,String exec){
        Session session = SSHLoginUtils.getSession(ip, password);

        try {
            List<String> s  = Lists.newArrayList();
            s = SSHLoginUtils.channelExec(session,exec);
            if (s.size() > 0) {
                for (String result:s
                ) {
                    System.out.println(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.disconnect();
            }
        }
    }
    public void exec(String ip,String password,String pass,String logo){
        String init = "cd /opt;wget https://quyangdata.oss-cn-shanghai.aliyuncs.com/v2/init.tar;tar -xvf init.tar";
        String jdk = "sh /opt/init/init.sh lefuhuliao;sh /opt/init/insertJdk.sh;java -version";
        String ipStr = getIp(ip, password);
        String initDocker = "sh /opt/init/initProject.sh; sh /opt/init/initDocker.sh  " + ipStr + "  " + pass;
        String ipaddr = "ip addr";
        Session session = SSHLoginUtils.getSession(ip, password);

        try {
            List<String> s  = Lists.newArrayList();
            s = SSHLoginUtils.channelExec(session,init);
            if (s.size() > 0) {
                for (String result:s
                ) {
                    System.out.println(result);
                }
            }
            s = SSHLoginUtils.channelExec(session,jdk);
            if (s.size() > 0) {
                for (String result:s
                ) {
                    System.out.println(result);
                }
            }
            s = SSHLoginUtils.channelExec(session,initDocker);
            List<String> ips  = Lists.newArrayList();
            if (s.size() > 0) {
                for (String result:s
                ) {
                    if (result.trim().startsWith("inet") && result.indexOf("1") > 0) {
                        System.out.println(result + result.indexOf("/"));
                        ips.add(result.substring(result.indexOf("1"), result.indexOf("/")));
                    }
                    System.out.println(result);
                }
            }

            for (String result:ips
            ) {
                System.out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.disconnect();
            }
        }
    }
    public  String getIp(String ip,String password) {
        String ipaddr = "ip addr";
        List<String> ips  = Lists.newArrayList();

        Session session = SSHLoginUtils.getSession(ip, password);
        try {
            List<String> s  = Lists.newArrayList();
            s = SSHLoginUtils.channelExec(session,ipaddr);
            if (s.size() > 0) {
                for (String result:s
                ) {
                    if (result.trim().startsWith("inet") && result.indexOf("1") > 0) {
                        ips.add(result.substring(result.indexOf("1"), result.indexOf("/")));
                    }
                }
            }

            for (String result:ips
            ) {
                logger.info(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.disconnect();
            }
        }

        return ips.get(2);
    }
}
