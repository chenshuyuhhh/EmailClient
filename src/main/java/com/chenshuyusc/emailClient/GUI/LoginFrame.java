package com.chenshuyusc.emailClient.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame {

    private JTextField userText;
    private JPasswordField passwordText;
    private JTextField popText;
    private JTextField smtpText;
    private JTextField popPortText;
    private JTextField smtpPortText;
    private JFrame frame;

    public LoginFrame() {
        initView();
    }

    public void setVisible(boolean bool){
        frame.setVisible(bool);
    }

    public void setDefaultCloseOperation(int operation){
        frame.setDefaultCloseOperation(operation);
    }

    public void initView() {

        // 创建 JFrame 实例
        frame = new JFrame("Email Login");
        // Setting the width and height of frame
        frame.setSize(350, 210);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* 创建面板，这个类似于 HTML 的 div 标签
         * 我们可以创建多个面板并在 JFrame 中指定位置
         * 面板中我们可以添加文本字段，按钮及其他组件。
         */
        JPanel panel = new JPanel();
        // 添加面板
        frame.add(panel);
        /*
         * 调用用户定义的方法并添加组件到面板
         */
        placeComponents(panel);

        // 设置界面可见
        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {

        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        panel.setLayout(null);

        // 创建 JLabel
        JLabel userLabel = new JLabel("User:");
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        /*
         * 创建文本域用于用户输入
         */
        userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
       // userText.setText("2468493317@qq.com");
        panel.add(userText);

        // 输入密码的文本域
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        /*
         *这个类似用于输入的文本域
         * 但是输入的信息会以点号代替，用于包含密码的安全性
         */
        passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 165, 25);
      //  passwordText.setText("tnxixwwvtvobdjjh");
        panel.add(passwordText);

        // pop
        JLabel popLabel = new JLabel("pop");
        popLabel.setBounds(10, 80, 80, 25);
        panel.add(popLabel);

        /*
         * pop 的文本域
         */
        popText = new JTextField(20);
        popText.setText("pop.qq.com");
        popText.setBounds(100, 80, 100, 25);
        panel.add(popText);

        // pop port
        JLabel popPortLabel = new JLabel("port");
        popPortLabel.setBounds(210, 80, 50, 25);
        panel.add(popPortLabel);

        /*
         * pop port 的文本域
         */
        popPortText = new JTextField(20);
        popPortText.setText("110");
        popPortText.setBounds(260, 80, 50, 25);
        panel.add(popPortText);

        // smtp
        JLabel smtpLabel = new JLabel("smtp");
        smtpLabel.setBounds(10, 110, 80, 25);
        panel.add(smtpLabel);

        /*
         * smtp 的文本域
         */
        smtpText = new JTextField(20);
        smtpText.setText("smtp.qq.com");
        smtpText.setBounds(100, 110, 100, 25);
        panel.add(smtpText);

        // smtp port
        JLabel smtpPortLabel = new JLabel("port");
        smtpPortLabel.setBounds(210, 110, 50, 25);
        panel.add(smtpPortLabel);

        /*
         * smtp port 的文本域
         */
        smtpPortText = new JTextField(20);
        smtpPortText.setText("25");
        smtpPortText.setBounds(260, 110, 50, 25);
        panel.add(smtpPortText);


        // 创建登录按钮
        JButton loginButton = new JButton("login");
        loginButton.setBounds(200, 140, 80, 25);
        loginButton.setActionCommand("OK");

        loginButton.addActionListener(btnActionListener);
        panel.add(loginButton);
    }

    private ActionListener btnActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            String s = e.getActionCommand();
            if ("OK".equals(s)) {
                // 获得登录的信息
                String urser = userText.getText();
                String pass = String.valueOf(passwordText.getPassword());
                String smtp = smtpText.getText();
                String pop = popText.getText();
                String popPort = popPortText.getText();
                String smtpPort = popPortText.getText();
                MainFrame mainFrame = new MainFrame(urser,pass,smtp,pop,smtpPort,popPort,LoginFrame.this);
                mainFrame.onCreate();
            }
        }
    };

}
