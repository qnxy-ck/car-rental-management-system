package com.qnxy.window;


import javax.swing.*;
import java.awt.*;
import java.util.Optional;

/**
 * 输入框组件
 * <p>
 * 例:
 * 用户名: 输入框
 *
 * @author Qnxy
 */
public class LabelTextField extends JPanel {

    /**
     * 输入框前提示标签
     */
    private final JLabel label;

    /**
     * 输入框
     */
    private final JTextField input;

    private final Object defaultValue;


    public LabelTextField(String labelName, SetInputValueDocumentListener valueSet) {
        this(labelName, null, true, false, valueSet);
    }

    public LabelTextField(String labelName, boolean isPassword, SetInputValueDocumentListener valueSet) {
        this(labelName, null, true, isPassword, valueSet);
    }

    public LabelTextField(String labelName, Object defaultValue, boolean editable, SetInputValueDocumentListener valueSet) {
        this(labelName, defaultValue, editable, false, valueSet);
    }

    public LabelTextField(String labelName, Object defaultValue, SetInputValueDocumentListener valueSet) {
        this(labelName, defaultValue, true, false, valueSet);
    }


    /**
     * 构建该组件
     *
     * @param labelName  输入框前提示标签
     * @param isPassword 是否为密码输入框
     * @param valueSet   输入框内容变更事件调用接口, 将输入的值传递给调用者
     */
    public LabelTextField(String labelName, Object defaultValue, boolean editable, boolean isPassword, SetInputValueDocumentListener valueSet) {
        this.label = new JLabel(labelName);
        this.input = isPassword ? new JPasswordField() : new JTextField();
        this.defaultValue = defaultValue;

        // 监听输入框的输入更新事件
        input.getDocument().addDocumentListener(valueSet);

        input.setText(Optional.ofNullable(defaultValue).orElse("").toString());
        input.setEditable(editable);
        if (!editable) {
            input.setBackground(new Color(217, 214, 214));
            input.setToolTipText("不可编辑");
        }

        this.initPanel();
    }

    private void initPanel() {
        // 设置该窗口的大小
        setPreferredSize(new Dimension(300, 38));
        setLayout(null);

        // 设置元素绝对定位
        label.setBounds(0, 0, 100, 38);
        input.setBounds(100, 0, 200, 38);

        // 添加到面板
        add(label);
        add(input);
    }


    /**
     * 如果存在默认值的话, 恢复输入框默认值
     */
    public void restoreDefaultText() {
        input.setText(this.defaultValue == null ? null : this.defaultValue.toString());
    }

}
