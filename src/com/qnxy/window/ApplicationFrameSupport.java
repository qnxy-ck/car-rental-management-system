package com.qnxy.window;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

/**
 * 启动页默认行为定义
 *
 * @author Qnxy
 */
public abstract class ApplicationFrameSupport extends JFrame {
    

    public ApplicationFrameSupport(ChildPanelSupport initPanel) throws HeadlessException {
        setGlobalDefaultBehavior();

        // 冻结窗口, 不可调整窗口大小
        setResizable(false);
        addComponent(initPanel);
        setVisible(true);
    }

    
    public void addComponent(ChildPanelSupport component) {
        component.setRootFrame(this);
        add(component);
        redraw();
    }

    /**
     * 重新绘制页面
     */
    private void redraw() {
        // 根据该页面中的组件进行重新绘制窗口大小
        // 具体大小根据子组件的大小决定
        pack();
        // 重新设置居中
        setLocationRelativeTo(null);
    }

    /**
     * 统一设置默认行为，父界面设置之后，所有由父界面进入的子界面都不需要再次设置
     * 但可以重新设置覆盖
     */
    private static void setGlobalDefaultBehavior() {
        // 字体
        final Font fontRes = new Font("宋体", Font.BOLD, 16);
        // 背景色
        final Color background = new Color(237, 239, 242);
        // 前景色(字体)
        final Color foreground = new Color(31, 38, 51);


        final Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            final Object key = keys.nextElement();
            final Object value = UIManager.get(key);

            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }

            if (key instanceof String) {
                final String k = (String) key;
                if (k.endsWith(".background")) {
                    UIManager.put(key, background);
                }

                if (k.endsWith(".foreground")) {
                    UIManager.put(key, foreground);
                }

            }
        }
    }

}
