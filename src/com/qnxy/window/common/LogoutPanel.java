package com.qnxy.window.common;

import com.qnxy.window.ChildPanelSupport;
import com.qnxy.window.login.LoginPanel;

import javax.swing.*;
import java.awt.*;

/**
 * 退出登录按钮及退出响应的事件
 * 
 * @author Qnxy
 */
public class LogoutPanel extends JPanel {

    private final ChildPanelSupport parent;

    public LogoutPanel(ChildPanelSupport parent) {
        super(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        this.parent = parent;

        final JButton button = new JButton("退出登录");
        button.setBackground(new Color(255, 254, 240));
        button.addActionListener(e -> logoutAction());

        add(button);
    }

    private void logoutAction() {
        final int dialogFlag = JOptionPane.showConfirmDialog(
                parent,
                "确定退出吗!",
                "是否确认退出",
                JOptionPane.YES_NO_OPTION
        );

        if (dialogFlag == JOptionPane.YES_OPTION) {
            parent.removeThisAndAdd(new LoginPanel());
        }
    }
}
