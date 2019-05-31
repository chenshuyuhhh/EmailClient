package com.chenshuyusc.emailClient.controller;

import com.chenshuyusc.emailClient.Entity.Mail;
import com.chenshuyusc.emailClient.utils.MyBase64;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 负责发邮件
 */
public class SmtpSender {
    private final int smtpPort = 25;
    private String smtpServer = "smtp.qq.com";
    private String myEmailAddr = "";
    private String myEmailPass = "";
    private Socket smtp;
    private BufferedReader smtpIn;
    private PrintWriter smptOut;

    public SmtpSender(String smtpServer, String myEmailAddr,
                      String myEmailPass) {
        super();
        this.smtpServer = smtpServer;
        this.myEmailAddr = myEmailAddr;
        this.myEmailPass = myEmailPass;

        loginSmtpServer();

    }

    /**
     * 执行认证到服务器
     */
    public void loginSmtpServer() {
        try {
            smtp = new Socket(smtpServer, smtpPort);
            smtpIn = new BufferedReader(new InputStreamReader(smtp.getInputStream()));
            smptOut = new PrintWriter(smtp.getOutputStream());
            String res = smtpIn.readLine();
            System.out.println("🌝 S: " + res);
            //HELO 向服务器标识用户身份
            send("HELO " + myEmailAddr);
            //AUTH
            send("auth login");
            send(MyBase64.getBASE64(myEmailAddr));
            send(MyBase64.getBASE64(myEmailPass));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行退出连接
     */
    public void quitSmtpServer() {
        try {
            //QUIT
            send("QUIT");
            smtp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行发送
     *
     * @param mail
     * @return
     * @throws IOException
     */
    public boolean send(Mail mail) {
        try {
            //MAIL FROM
            send("MAIL FROM:" + myEmailAddr);

            //RCPT TO
            for (int i = 0; i < mail.getStringList().size(); i++) {
                send("RCPT TO:" + mail.getStringList().get(i));
            }

            //DATA
            send("DATA");
            smptOut.print(mail.getDataString());
            return send(".");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    //发送命令并接收回应
    private boolean send(String command) throws IOException {
        smptOut.print(command + "\r\n");
        smptOut.flush();
        System.out.println("🌚 C: " + command);
        String res = smtpIn.readLine();
        System.out.println("🌝 S: " + res);
        return (res.substring(0, 3).equals("250"));
    }
}
