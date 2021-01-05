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
public class ImXxlJob {
    private static Logger logger = LoggerFactory.getLogger(ImXxlJob.class);

    /**
     * 输入Cookic
     * 第1位为密码，,分隔后面的为解析地址
     */
    @XxlJob("imIp")
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
    @XxlJob("im")
    public ReturnT<String> im(String param) throws Exception {
        if (StringUtils.isEmpty(param) || param.split(",").length < 1) {
            XxlJobLogger.log("参数不正确");
            return ReturnT.FAIL;
        }
        param = param.replace("\n", "");
        String[] params = param.split(",");


        String initDir = "rm -rf /opt/*";
        exec(params[0],params[1],initDir);


//        wget http://47.57.69.130/soft/init.tar
        String init = "cd /opt;wget http://47.57.69.130/soft/init.tar; tar -xvf init.tar";
        String down = "sh /opt/init/init.sh " + params[3];
        String insertJdk = "sh /opt/init/insertJdk.sh;";
        String ipStr = getIp(params[0],params[1]);
        String initDocker = "sh /opt/init/initDocker.sh  " + ipStr + "  " + params[2];
        String ipaddr = "ip addr";


        XxlJobLogger.log("ipStr:" + ipStr);
        logger.info("ipStr:" + ipStr);

        XxlJobLogger.log("系统更新-开始执行");
        String updateStr = "yes yes | yum update ";
        exec(params[0],params[1],updateStr);
        XxlJobLogger.log("系统更新-结束执行");


        XxlJobLogger.log("初始化-开始执行");
        exec(params[0],params[1],init);
        XxlJobLogger.log("初始化-结束执行");

        XxlJobLogger.log("下载项目-开始执行");
        exec(params[0],params[1],down);
        XxlJobLogger.log("下载项目-结束执行");


        XxlJobLogger.log("下载软件-开始执行");
        String mongo = "wget http://47.57.69.130/soft/mongodb-linux-x86_64-3.4.0.tgz ";
        exec(params[0],params[1],mongo);
        String redis = "wget http://47.57.69.130/soft/redis-4.0.1.tar  ";
        exec(params[0],params[1],redis);
        String rocketmq = "wget http://47.57.69.130/soft/rocketmq-all-4.3.2-bin-release.tar";
        exec(params[0],params[1],rocketmq);
        String jdk8u131 = "wget http://47.57.69.130/soft/jdk-8u131-linux-x64.tar.gz";
        exec(params[0],params[1],jdk8u131);
        XxlJobLogger.log("下载软件-结束执行");

        XxlJobLogger.log("JDK-开始执行");
        exec(params[0],params[1],insertJdk);
        XxlJobLogger.log("JDK-结束执行");


        XxlJobLogger.log("initProject-开始执行");
        String initProject = "sh /opt/init/initProject.sh";
        exec(params[0],params[1],initProject);
        XxlJobLogger.log("initProject-结束执行");

        XxlJobLogger.log("依赖软件安装insertMongo-开始执行");
        String insertMongo = "sh /opt/init/insertMongo.sh";
        exec(params[0],params[1],insertMongo);
        XxlJobLogger.log("依赖软件安装insertMongo-结束执行");

        XxlJobLogger.log("依赖软件安装docker-开始执行");
        String docker = "yes yes | yum install docker;systemctl start docker && systemctl enable docker";
        exec(params[0],params[1],docker);
        XxlJobLogger.log("依赖软件安装docker-结束执行");

        XxlJobLogger.log("依赖软件安装initDocker-开始执行");
        exec(params[0],params[1],initDocker);
        XxlJobLogger.log("依赖软件安装initDocker-结束执行");

        return ReturnT.SUCCESS;
    }


    /**
     * ip0,pass1,域名2，名称3，key4，logo5
     * 没有密码，自动登陆
     * 前两个参数为账号和密码
     * ,分隔解析地址
     */
    @XxlJob("imStart")
    public ReturnT<String> imStart(String param) throws Exception {
        if (StringUtils.isEmpty(param) || param.split(",").length < 1) {
            XxlJobLogger.log("参数不正确");
            return ReturnT.FAIL;
        }
        param = param.replace("\n", "");
        String[] params = param.split(",");

        XxlJobLogger.log("mongodb-开始执行");
        String mongoStr = "export JAVA_HOME=/opt/java/jdk1.8.0_131;cd /opt/mongodb-3.4.0; sh stop; sh start ";
        exec(params[0],params[1],mongoStr);
        XxlJobLogger.log("mongodb-结束执行");
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

            XxlJobLogger.log("项目启动-开始执行" + ":" + start);
            exec(params[0],params[1],start);
            XxlJobLogger.log("项目启动-结束执行");
        }else{
            String start = "export JAVA_HOME=/opt/java/jdk1.8.0_131;sh /opt/init/initStart.sh  " + params[2] +  " " +  ipStr +  " " + params[3] +  " " + ipStr +  " " + params[4] +  " " + params[5];

            XxlJobLogger.log("项目启动-开始执行" + ":" + start);
            exec(params[0],params[1],start);
            XxlJobLogger.log("项目启动-结束执行");
        }
        if (params[5].indexOf("jpg") > 0) {
            String logo = "sh /opt/init/logo.sh " +  params[5];
            XxlJobLogger.log("logo替换-开始执行");
            exec(params[0],params[1],logo);
            XxlJobLogger.log("logo替换-结束执行");
        }

        String dockerRestart = "docker restart redis;docker restart rmqnamesrv_mq && docker restart rmqbroker_mq;docker restart nginx ";
        exec(params[0],params[1],dockerRestart);
        XxlJobLogger.log("项目启动tigase-开始执行");
        exec(params[0],params[1],tigase);
        XxlJobLogger.log("项目启动tigase-结束执行");

        XxlJobLogger.log("项目启动imapi-开始执行");
        exec(params[0],params[1],imapi);
        XxlJobLogger.log("项目启动imapi-结束执行");

        XxlJobLogger.log("项目启动upload-开始执行");
        exec(params[0],params[1],upload);
        XxlJobLogger.log("项目启动upload-结束执行");

        XxlJobLogger.log("项目启动message-开始执行");
        exec(params[0],params[1],message);
        XxlJobLogger.log("项目启动message-结束执行");


        XxlJobLogger.log("项目启动shikupush-开始执行");
        exec(params[0],params[1],shikupush);
        XxlJobLogger.log("项目启动shikupush-结束执行");

        XxlJobLogger.log("项目启动mpserver-开始执行");
        exec(params[0],params[1],mpserver);
        XxlJobLogger.log("项目启动mpserver-结束执行");

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
