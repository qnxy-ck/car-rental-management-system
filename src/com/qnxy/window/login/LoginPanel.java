package com.qnxy.window.login;

import com.qnxy.common.LoginType;
import com.qnxy.window.ChildPanelSupport;
import com.qnxy.window.LabelTextField;
import com.qnxy.window.RadioButtonGroup;
import com.qnxy.window.admin.AdministratorPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.qnxy.window.util.DiaLogUtil.showInProgress;

/**
 * 用户登录窗口
 *
 * @author Qnxy
 */
public final class LoginPanel extends ChildPanelSupport {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户选择的登陆选项
     */
    private LoginType selectedRadioType;

    @Override
    protected void initialization(ParentFrameScope parentFrameScope) {
        parentFrameScope.setWindowTitle("用户注册");
        parentFrameScope.setDefaultCloseOperation(CloseOperation.EXIT_ON_CLOSE);

        // 设置当前面板的信息
        setLayout(new BorderLayout());
        // 使用 BorderLayout 进行上中下布局
        setPreferredSize(new Dimension(400, 300));


        add(inputPanel(), BorderLayout.NORTH);
        add(radioButtonPanel(), BorderLayout.CENTER);
        add(loginAndRegisterPanel(), BorderLayout.SOUTH);
    }


    /**
     * 输入框组件
     */
    private JPanel inputPanel() {
        // 使用 FlowLayout 进行面板布局
        // 使内部组件进行居中
        // 父组件宽度无法在一行同时容纳两个组件时, 第二个组件会自动流动到第二行
        return new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30)) {{
            // 设置该面板总高度, 容纳两个组件
            setPreferredSize(new Dimension(0, 160));
            add(new LabelTextField("用户名:", it -> username = it));
            add(new LabelTextField("密  码:", true, it -> password = it));
        }};
    }

    /**
     * 登陆选项单选按钮信息
     */
    private static Map<String, LoginType> radioButtonInfoMap() {
        return Arrays.stream(LoginType.values())
                .collect(Collectors.toMap(LoginType::getTypeText, Function.identity()));
    }

    /**
     * 选择框组件
     */
    private JPanel radioButtonPanel() {
        final FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(40);
        final JPanel radioButtonPanel = new JPanel(flowLayout);

        new RadioButtonGroup<>(
                radioButtonPanel,
                radioButtonInfoMap(),
                LoginType.ADMINISTRATOR,
                it -> this.selectedRadioType = it
        );

        return radioButtonPanel;
    }

    /**
     * 登陆/注册按钮组件
     */
    private JPanel loginAndRegisterPanel() {
        final FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(70);
        return new JPanel(flowLayout) {{
            setPreferredSize(new Dimension(0, 70));

            final JButton loginButton = new JButton("登陆");
            final JButton registerButton = new JButton("注册");

            loginButton.setBackground(new Color(230, 253, 255));
            loginButton.addActionListener(loginActionListener());
            registerButton.addActionListener(registerActionListener());

            add(loginButton);
            add(registerButton);
        }};
    }

    /**
     * 登录事件
     */
    private ActionListener loginActionListener() {
        return e -> {
            printVal();

            switch (LoginPanel.this.selectedRadioType) {
                case USER:
                    showInProgress(LoginPanel.this);
                    break;
                case ADMINISTRATOR:
                    super.removeThisAndAdd(new AdministratorPanel());
            }

        };
    }

    /**
     * 注册事件
     */
    private ActionListener registerActionListener() {
        return e -> {
            printVal();

            showInProgress(LoginPanel.this);
        };
    }


    private void printVal() {
        System.out.println("账户: " + username + " 密码: " + password + " 选择: " + selectedRadioType);
    }

}
