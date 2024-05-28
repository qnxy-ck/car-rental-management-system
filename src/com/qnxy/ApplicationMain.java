package com.qnxy;

import com.qnxy.window.ApplicationFrameSupport;
import com.qnxy.window.login.LoginPanel;

import java.awt.*;

/**
 * 主启动窗口
 *
 * @author Qnxy
 */
public final class ApplicationMain extends ApplicationFrameSupport {

    public ApplicationMain() throws HeadlessException {
        // 添加默认窗口为登陆窗口
        super(new LoginPanel());
    }

    public static void main(String[] args) {
        new ApplicationMain();
    }

}
