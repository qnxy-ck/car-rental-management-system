package com.qnxy.window.util;

import javax.swing.*;
import java.awt.*;

/**
 * @author Qnxy
 */
public final class DiaLogUtil {
    
    public static void showInProgress(Component parentComponent) {
        JOptionPane.showMessageDialog(
                parentComponent,
                "正在努力实现中...",
                "提示",
                JOptionPane.ERROR_MESSAGE
        );
    }
    
    
}
