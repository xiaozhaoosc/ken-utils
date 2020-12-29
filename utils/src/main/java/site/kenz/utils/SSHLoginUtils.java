package site.kenz.utils;

import com.google.common.collect.Lists;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * </ul>
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-16
 */
public class SSHLoginUtils {

    public static final String COMMAND_SU       = "su";
    public static final String COMMAND_SH       = "sh";
    public static final String COMMAND_EXIT     = "exit\n";
    public static final String COMMAND_LINE_END = "\n";

    public static void main(String[] args) {
        String ip = "8.129.131.24";
        String password = "aIK2rUiKopkVNO5q";
        String init = "cd /opt;wget https://quyangdata.oss-cn-shanghai.aliyuncs.com/v2/init.tar;tar -xvf init.tar";
        String jdk = "sh /opt/init/init.sh lefuhuliao;sh /opt/init/insertJdk.sh;java -version";
        String initDocker = "sh /opt/init/initProject.sh; sh /opt/init/initDocker.sh 172.31.173.73  lfhl1220";
        String ipaddr = "ip addr";
        Session session = getSession(ip, password);

        try {
            List<String> s  = Lists.newArrayList();
//            s = channelExec(session,init);
//            if (s.size() > 0) {
//                for (String result:s
//                     ) {
//                    System.out.println(result);
//                }
//            }
//            s = channelExec(session,jdk);
//            if (s.size() > 0) {
//                for (String result:s
//                ) {
//                    System.out.println(result);
//                }
//            }
            s = channelExec(session,initDocker);
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
    /**
     * 打开exec通道
     */
    public static List<String> channelExec(Session session,String commands) {
        ChannelExec channelExec = getChannelExec(session);
        List<String> s = Lists.newArrayList();
        try {
            channelExec.setCommand(commands);
            channelExec.connect();
            InputStream inputStream = channelExec.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                s.add(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channelExec.disconnect();
            session.disconnect();
        }
        return  s;
    }
    /**
     * 打开exec通道
     */
    public static ChannelExec getChannelExec(Session session) {
        ChannelExec channel = null;
        try {
            //设置通道类型
            channel = (ChannelExec) session.openChannel("exec");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channel;
    }
    /**
     * 登陆并打开服务器会话
     * @param ip       目标主机名或ip
     */
    private static Session getSession(String ip, String password) {
        Session session = getSession(ip, "root", password, 22);
        return session;
    }
    /**
     * 登陆并打开服务器会话
     * @param ip       目标主机名或ip
     */
    private static Session getSession(String ip, String username, String password, Integer port) {
        Session session = null;
        JSch jSch = new JSch();
        try {
            if (port != null) {
                session = jSch.getSession(username, ip, port.intValue());
            } else {
                session = jSch.getSession(username, ip);
            }
            if (password != null) {
                session.setPassword(password);
            }
            //关闭主机密钥检查，否则会导致连接失败
            Properties properties = new Properties();
            properties.setProperty("StrictHostKeyChecking", "no");
            session.setConfig(properties);
            //打开会话，并设置超时时间
            session.connect(30000000);
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return session;
    }
}
