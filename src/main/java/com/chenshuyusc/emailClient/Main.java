package com.chenshuyusc.emailClient;

import com.chenshuyusc.emailClient.GUI.LoginFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        try {
            LoginFrame dialog = new LoginFrame();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
