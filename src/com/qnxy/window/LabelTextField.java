package com.qnxy.window;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 输入框组件
 * <p>
 * 例:
 * 用户名: 输入框
 *
 * @author Qnxy
 */
public class LabelTextField extends JPanel implements DocumentListener {

    /**
     * 输入框前提示标签
     */
    private final JLabel label;

    /**
     * 输入框
     */
    private final JTextField input;

    /**
     * 输入框内容变更事件调用接口
     */
    private final Consumer<String> valueSet;

    private final Object defaultValue;


    public LabelTextField(String labelName, Consumer<String> valueSet) {
        this(labelName, null, true, false, valueSet);
    }

    public LabelTextField(String labelName, boolean isPassword, Consumer<String> valueSet) {
        this(labelName, null, true, isPassword, valueSet);
    }

    public LabelTextField(String labelName, Object defaultValue, boolean editable, Consumer<String> valueSet) {
        this(labelName, defaultValue, editable, false, valueSet);
    }

    public LabelTextField(String labelName, Object defaultValue, Consumer<String> valueSet) {
        this(labelName, defaultValue, true, false, valueSet);
    }


    /**
     * 构建该组件
     *
     * @param labelName  输入框前提示标签
     * @param isPassword 是否为密码输入框
     * @param valueSet   输入框内容变更事件调用接口, 将输入的值传递给调用者
     */
    public LabelTextField(String labelName, Object defaultValue, boolean editable, boolean isPassword, Consumer<String> valueSet) {
        this.label = new JLabel(labelName);
        this.input = isPassword ? new JPasswordField() : new JTextField();
        this.valueSet = valueSet;
        this.defaultValue = defaultValue;

        // 监听输入框的输入更新事件
        input.getDocument().addDocumentListener(this);

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

    @Override
    public void insertUpdate(DocumentEvent e) {
        // 当向输入框输入内容时, 会调用该方法
        Optional.ofNullable(this.valueSet)
                .ifPresent(it -> it.accept(input.getText()));
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        // 删除时调用
        Optional.ofNullable(this.valueSet)
                .ifPresent(it -> it.accept(input.getText()));
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
}
