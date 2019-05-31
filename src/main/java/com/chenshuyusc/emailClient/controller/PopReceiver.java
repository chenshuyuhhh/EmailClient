package com.chenshuyusc.emailClient.controller;


import com.chenshuyusc.emailClient.Entity.Mail;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 通过 pop3 协议来收邮件
 */
public class PopReceiver {
    private int popPort = 110;
    private String popServer = "";
    private String myEmailAddr = "";
    private String myEmailPass = "";
    private Socket pop;
    private InputStream popIn;
    private BufferedReader popReader;
    private PrintWriter popOut;

    public PopReceiver(String popServer, String myEmailAddr,
                       String myEmailPass, int popPort) {
        super();
        this.popServer = popServer;
        this.myEmailAddr = myEmailAddr;
        this.myEmailPass = myEmailPass;
        this.popPort = popPort;

        loginPopServer();
    }

    /**
     * 连接到服务器
     */
    private void loginPopServer() {
        try {

            // 先初始化建立链接的 socket，和用于读取的 I/O 流
            pop = new Socket(popServer, popPort);
            popIn = pop.getInputStream();
            popReader = new BufferedReader(new InputStreamReader(popIn));
            // 邮件客户端的输出用 writer 方便发送
            popOut = new PrintWriter(pop.getOutputStream());

            // 读取登陆回复，看 socket 链接是否成功建立
            readReply();

            // 发送用户名和密码
            sendAndGet("user " + myEmailAddr);
            sendAndGet("pass " + myEmailPass);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得邮件总数
     *
     * @return
     */
    public int getMailCount() {
        String count = sendAndGet("stat").split(" ")[1];
        return Integer.valueOf(count);
    }

//    public Mail getOneEmail(String number) {
//        String mailStr = sendAndGet("retr " + number);
//        Mail mail = new Mail(new ArrayList<>());
//        return mail;
//    }

    public List<String> getItemString(String number) {
        return getLines("RETR " + number);
    }

    /**
     * 处理只有一行回复的命令
     *
     * @param command
     * @return
     */
    private String sendAndGet(String command) {
        try {
            // send
            send(command);
            // read
            String str = readReply();
            return str;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 进行多行回复命令的处理
     *
     * @return
     */
    private List<String> getLines(String command) {
        List<String> lines = new ArrayList<>();
        try {
            boolean cont = true;
            String buf = null;

            // 发送命令
            popOut.print(command + "\r\n");
            popOut.flush();
            System.out.println("👉 C: " + command);
            // 读取回复
            String res = popReader.readLine();
            lines.add(res);

            System.out.println("👈 S: " + res);
            //  如果传回的回复不是 + OK ...
            if (!("+OK".equals(res.substring(0, 3)))) {
                //pop.close();
                //System.out.println("关闭？");
                return lines;
            }

            while (cont) {
                buf = popReader.readLine();
                lines.add(buf);

                System.out.println("👈 S: " + buf);
                // 用一行开头是单一句号结束
                if (".".equals(buf))
                    cont = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * 向服务器发送请求
     *
     * @param command
     */
    private void send(String command) {
        popOut.print(command + "\r\n");
        popOut.flush();
        System.out.println("👉 C: " + command);
    }

    /**
     * 读取回复
     */
    private String readReply() throws IOException {
        String str;
        // 读取回复
        int count = 0;
        while (count == 0) {
            count = popIn.available();
        }
        byte[] b = new byte[count];
        popIn.read(b);
        str = new String(b);

       // String str = popReader.readLine();

        System.out.println("👈 S: " + str);
        //  如果传回的回复不是 + OK ....
        if (!("+OK".equals(str.substring(0, 3)))) {
            pop.close();
            System.out.println("不是 OK，哭了");
        }
        return str;
    }

    /**
     * pop3 对话结束
     */
    public void quitPop() {
        // QUIT
        sendAndGet("QUIT");
        try {
            pop.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
