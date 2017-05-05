package destekinfo.com.commandrunner.utils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.ByteArrayOutputStream;
import java.util.Properties;

public class Utils {

    public static String restartApp(
            String username,
            String password,
            String hostname,
            String hostName,
            String appName,
            String jop) throws Exception {

        String command = "bash /home/ansible/ansible_pb/sh/" + jop + " " + hostName + " " + appName;
        return runCommand(username, password, hostname, command);
    }


    public static String deployApp(
            String username,
            String password,
            String hostname,
            String jop) throws Exception {

        String command = "curl -X POST -u user:55975d8dce704ef377ac847c1a0f8222 http://localhost:9090/job/" + jop + "/build";
        return runCommand(username, password, hostname, command);
    }

    private static String runCommand(String username,
                                     String password,
                                     String hostname,
                                     String command) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, hostname, 22);
        session.setPassword(password);

        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);

        session.connect();

        ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channelssh.setOutputStream(baos);

        channelssh.setCommand(command);
        channelssh.connect();
        channelssh.disconnect();

        return baos.toString();
    }
}
