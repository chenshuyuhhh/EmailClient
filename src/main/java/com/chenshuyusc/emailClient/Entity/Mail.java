package com.chenshuyusc.emailClient.Entity;

import com.chenshuyusc.emailClient.Main;
import com.chenshuyusc.emailClient.utils.MyBase64;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mail {
    private String subject = ""; // 主题
    private String from = ""; // 发件人
    private String time = ""; // 时间
    private List<String> stringList = new ArrayList<String>();
    private String content; // 内容
    private String me; // 收件人

    public Mail() {

    }

    public Mail(String subject, String from, List<String> stringList,
                String content, String me) {
        super();
        this.subject = subject;
        this.from = from;
        this.stringList = stringList;
        this.content = content;
        this.me = me;
    }

    public Mail(List<String> lines, String me) {
        super();
        this.me = me;
        initByLines(lines);
        getOtherMsg(lines);
    }

    private void getOtherMsg(List<String> stringList) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        StringBuffer all = new StringBuffer();
        for (int i = 0; i < stringList.size(); i++) {
            all.append(stringList.get(i)).append("\r\n");
        }
        String allStr = all.toString();
        time = allStr.split("for")[1].split("; ")[1].split("\r\n")[0];
        from = allStr.split("X-QQ-ORGSender: ")[1].split("\r\n")[0];
//        subject = allStr.split("Subject: ")[1].split("\r\n")[0];
//        if (Pattern.matches(base64Pattern, subject)) {
//            subject = MyBase64.getFromBASE64(subject);
//        }
//        content = allStr.split("\r\n\r\n")[1].split(".")[0];
//        if (Pattern.matches(base64Pattern, content)) {
//            subject = MyBase64.getFromBASE64(content);
//        }
    }

    private void initByLines(List<String> lines) {
        //决定当前行是否解码
        boolean decodeWithBase64 = false;
        //排除前一行的干扰
        boolean isPreLine = false;
        //是否已经收集完内容  不要html
        boolean hasContent = false;
        StringBuffer temp = new StringBuffer("");
        for (int i = 0; i < lines.size(); i++) {
            String buf = lines.get(i);
            isPreLine = false;
            if (buf.startsWith("Return-Path:")) {
                String regex = "<(.*)>";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(buf);
                boolean cont = true;
                while (m.find()) {
                    if (cont) {
                        stringList = new ArrayList<>();
                        stringList.add(m.group(1));
                        cont = false;
                    }
                }
            } else if (buf.startsWith("Subject:")) {
                boolean isUTF8 = false;
                String regex = "=\\?(?i)UTF-8\\?B\\?(.*)\\?=";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(buf);
                while (m.find()) {
                    isUTF8 = true;
                    subject = MyBase64.getFromBASE64(m.group(1));
                }
                if (!isUTF8) subject = buf.substring(8);

            } else if (buf.startsWith("From:")) {
                from = buf.substring(5);
            } else if (buf.endsWith("base64")) {
                if (!hasContent) {
                    decodeWithBase64 = true;
                    isPreLine = true;
                }
                hasContent = true;
            } else if (buf.startsWith("--")) {
                decodeWithBase64 = false;
            }

            if (decodeWithBase64 && !isPreLine) {
                temp.append(MyBase64.getFromBASE64(buf));
            }
        }
        content = temp.toString();
    }

    public String getDataString() {
        String to = "";
        for (int i = 0; i < stringList.size(); i++) {
            to += stringList.get(i) + ";";
        }
        String result = "From:" + from + "\r\n" + "To:" + to + "\r\n"
                + "Subject:" + subject + "\r\n" + content + "\r\n";
        return result;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(Vector<String> stringList) {
        this.stringList = stringList;
    }

    public String getContent() {
        return content.toString();
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        String to = "";
        for (int i = 0; i < stringList.size(); i++) {
            to+= stringList.get(i)+";";
        }
        return "主题:"+subject+"\r\n"+
                "来自:"+from+"\r\n"+
                "时间:" + time + "\r\n" +
                "回复:"+to+"\r\n"+
                "正文:"+content;
    }
}
