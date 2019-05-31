package com.chenshuyusc.emailClient.GUI;

import com.chenshuyusc.emailClient.Entity.Mail;
import com.chenshuyusc.emailClient.controller.PopReceiver;
import com.chenshuyusc.emailClient.controller.SmtpSender;
import com.chenshuyusc.emailClient.utils.MyUtility;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录之后的收发界面
 */
public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTabbedPane tabbedPane;
    private JPanel contentPane;
    private JTextField addrsText;
    private JTextField subjectText;
    private JTextArea textArea;
    private JScrollPane scrollPaneText;
    private JScrollPane scrollPaneTable;
    private JTable table;
    private JButton back;
    private JEditorPane textAreaEmailbox;
    private JButton btnSend;
    private String addr;
    private String pass;
    private String smtpAddr;
    private String popAddr;
    private String smtpPort;
    private String popPort;

    private int curMailIndex;
    private ArrayList<Mail> mails = new ArrayList<Mail>();
    private PopReceiver popReceiver;
    private LoginFrame loginFrame;

    public MainFrame(String addr, String pass, String smtpAddr, String popAddr, String smtpPort, String popPort, LoginFrame loginFrame) {
        setResizable(false);
        this.loginFrame = loginFrame;
        this.addr = addr;
        this.pass = pass;
        this.smtpAddr = smtpAddr;
        this.popAddr = popAddr;
        this.smtpPort = smtpPort;
        this.popPort = popPort;
    }

    public void onCreate(){
        initView();

        // 开一个线程用于接收邮件
        new Thread(new Runnable() {
            public void run() {
                initPop();
            }
        }).start();
    }

    /**
     * 收取邮件
     */
    private void initPop() {
        popReceiver = new PopReceiver(popAddr, addr, pass, Integer.valueOf(popPort));
        int count = popReceiver.getMailCount();
        if (count == -1) {
            showMyDialog("Login failed!");
            this.setVisible(false);
        } else {
            loginFrame.setVisible(false);
            this.setVisible(true);
        }

        mails = new ArrayList<Mail>();
        Mail mail;
        for (int i = 1; i < count + 1; i++) {
            mail = new Mail(popReceiver.getItemString(i + ""),addr);
            mails.add(mail);
        }

        table.setModel(getTable(mails));
        table.setRowHeight(35);
        table.addMouseListener(new MouseAdapter() {
            /**
             * 鼠标点击事件
             */
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {
                    int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint()); // 获得行位置
                    curMailIndex = row;
                    textAreaEmailbox.setText(mails.get(row).toString());
                    setEmailContentVisible();
                }
            }

        });
    }

    private ActionListener listener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            String s = e.getActionCommand();
            if ("Send".equals(s)) {
                doSend();


            } else if ("back".equals(s)) {
                setTableVisible();

            }
        }
    };


    protected void initSmtpAndSend() {

        SmtpSender smtpSender = new SmtpSender(smtpAddr, addr,
                pass);

        List<String> list = MyUtility.getListFromString(addrsText.getText());

        Mail mail = new Mail(subjectText.getText(), addr, list, textArea.getText(),addr);
        String message = smtpSender.send(mail) ? "succeed" : "failed";
        smtpSender.quitSmtpServer();
        JOptionPane.showMessageDialog(MainFrame.this, message);
    }


    protected void doSend() {
        btnSend.setEnabled(false);
        btnSend.setText("请稍候..");
        new Thread(new Runnable() {
            public void run() {
                initSmtpAndSend();
                enableBtn();
            }
        }).start();
    }

    private void enableBtn() {
        btnSend.setEnabled(true);
        btnSend.setText("发送");
    }

    public void showMyDialog(String msg) {
        JOptionPane.showMessageDialog(MainFrame.this, msg);
    }

    private void setEmailContentVisible() {
        back.setVisible(true);
        scrollPaneText.setVisible(true);
        scrollPaneTable.setVisible(false);
    }

    private void setTableVisible() {
        back.setVisible(false);
        scrollPaneText.setVisible(false);
        scrollPaneTable.setVisible(true);
    }

    private TableModel getTable(final List<Mail> mails){
        // 插入表格
        TableModel model = new TableModel() {

            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            }

            public void removeTableModelListener(TableModelListener l) {
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                if (columnIndex == 0) {
                    return mails.get(rowIndex).getStringList().size() > 0 ? mails
                            .get(rowIndex).getStringList().get(0) : "匿名";
                }
                if (columnIndex == 1)
                    return mails.get(rowIndex).getSubject();
                return null;
            }

            public int getRowCount() {
                return mails.size();
            }

            public String getColumnName(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return "Sender";
                    case 1:
                        return "Title";
                    default:
                        break;
                }
                return null;
            }

            public int getColumnCount() {
                return 2;
            }

            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }

            public void addTableModelListener(TableModelListener l) {

            }
        };
        return model;
    }

    /**
     * 为主界面初始化布局
     */
    private void initView() {
        setTitle("CSYMailElient");

        // 统一字体
        Font font = new Font("Default", Font.PLAIN, 17);
        MyUtility.initGlobalFont(font);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 837, 585);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(0, 0, 831, 600);
        // tabbedPane.set
        contentPane.add(tabbedPane);

        JPanel panelWrite = new JPanel();
        panelWrite.setBounds(52, 239, 126, 96);

        JPanel panel_box = new JPanel();
        panel_box.setBounds(242, 203, 74, 154);

        tabbedPane.addTab("Write", new ImageIcon("src/main/resources/drawable/write.png"), panelWrite);
        panelWrite.setLayout(null);

        JLabel label = new JLabel("收信地址");
        label.setFont(new Font("Default", Font.PLAIN, 18));
        label.setBounds(26, 13, 129, 18);
        panelWrite.add(label);

        addrsText = new JTextField();
        addrsText.setFont(new Font("Default", Font.PLAIN, 14));
        addrsText.setBounds(137, 12, 412, 24);
        panelWrite.add(addrsText);
        addrsText.setColumns(10);

        JLabel label_1 = new JLabel("主 题");
        label_1.setFont(new Font("Default", Font.PLAIN, 18));
        label_1.setBounds(26, 44, 77, 18);
        panelWrite.add(label_1);

        subjectText = new JTextField();
        subjectText.setToolTipText("");
        subjectText.setFont(new Font("Default", Font.PLAIN, 14));
        subjectText.setColumns(10);
        subjectText.setBounds(137, 44, 412, 24);
        panelWrite.add(subjectText);

        textArea = new JTextArea();
        textArea.setBounds(26, 81, 688, 412);
        panelWrite.add(textArea);

        btnSend = new JButton("send");
        btnSend.setActionCommand("Send");
        btnSend.addActionListener(listener);
        btnSend.setBounds(620, 45, 90, 22);
        panelWrite.add(btnSend);

        tabbedPane.addTab("Receive", new ImageIcon("src/main/resources/drawable/receive.png"), panel_box);
        panel_box.setLayout(null);

        textAreaEmailbox = new JEditorPane();
        textAreaEmailbox.setBounds(96, 0, 578, 500);

        scrollPaneText = new JScrollPane(textAreaEmailbox);
        scrollPaneText.setBounds(0, 0, 777, 500);

        panel_box.add(scrollPaneText);

        back = new JButton("back");
        back.setActionCommand("back");
        back.addActionListener(listener);
        back.setBounds(632, 503, 93, 27);
        panel_box.add(back);

        scrollPaneTable = new JScrollPane();

        scrollPaneTable.setBounds(0, 0, 777, 550);
        panel_box.add(scrollPaneTable);

        table = new JTable();
        scrollPaneTable.setViewportView(table);

        setTableVisible();
    }

}
