package com.qnxy.window.common;

import com.qnxy.window.QuickListenerAdder;
import com.qnxy.window.SetInputValueDocumentListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

/**
 * 车辆租用详细内容提示框
 *
 * @author Qnxy
 */
public final class RentalTableDetailsDialog extends JDialog {

    private static final int width = 600;
    private static final int height = 400;

    /**
     * 是否为管理员使用
     */
    private final Boolean isAdmin;

    /**
     * 文本框初始内容
     */
    private final String initTextAreaValue;

    /**
     * 提交事件
     * 管理员使用, 管理员时不可为空
     */
    private final Consumer<String> submitAction;

    /**
     * 文本框每次修改时的值, 非管理员使用时和 initTextAreaValue 值相同
     * 提交时使用
     */
    private String textAreaValue;

    /**
     * 构建用户详情弹窗
     *
     * @param root 所属窗口
     * @param text 展示内容
     */
    public RentalTableDetailsDialog(JFrame root, String text) {
        this(root, text, null);
    }

    /**
     * 构建管理员详情弹窗
     *
     * @param root         所属窗口
     * @param text         展示内容
     * @param submitAction 提交事件, 不可为 null
     */
    public RentalTableDetailsDialog(JFrame root, String text, Consumer<String> submitAction) {
        super(root);
        this.isAdmin = submitAction != null;
        this.submitAction = submitAction;

        this.initTextAreaValue = text;
        this.textAreaValue = text;

        initRentalTableDetailsDialog();
        setVisible(true);
    }


    /**
     * 初始化 车辆租用详细内容提示框
     */
    private void initRentalTableDetailsDialog() {
        final String titlePrefix = isAdmin ? "管理员" : "用户";

        setResizable(false);

        setTitle(titlePrefix + "详细信息界面");
        setLayout(new BorderLayout());
        setSize(width, height);
        setLocationRelativeTo(null);

        // 数据展示
        add(textArea(), BorderLayout.CENTER);

        // 非管理员不展示底部按钮
        if (isAdmin) {
            add(bottomBtn(), BorderLayout.SOUTH);
        }

    }

    /**
     * 内容展示/编辑(仅管理员) 组件
     */
    public JComponent textArea() {
        final JTextArea jTextArea = new JTextArea(initTextAreaValue);

        // 防止展示内容贴靠边框太近, 加一点边距
        jTextArea.setBorder(new EmptyBorder(11, 11, 11, 11));
        // 管理员可编辑
        jTextArea.setEditable(isAdmin);
        // 使其可换行
        jTextArea.setLineWrap(true);

        // 仅管理员使用进行更新修改的内容
        if (isAdmin) {
            jTextArea.getDocument().addDocumentListener((SetInputValueDocumentListener) inputValue -> textAreaValue = inputValue);
        }

        // 使该多行文本可进行编辑
        return new JScrollPane(jTextArea);
    }

    /**
     * 底部按钮信息
     */
    private JPanel bottomBtn() {
        final JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 15));
        bottomPanel.setPreferredSize(new Dimension(width, 60));

        new QuickListenerAdder(bottomPanel)
                .add(new JButton("编辑/提交"), e -> this.submitAction())
                .add(new JButton("返回"), e -> this.dispose());

        return bottomPanel;
    }

    /**
     * 提交事件
     * 如果初始内容和修改后的内容相同, 则不进行更新
     */
    private void submitAction() {
        if (!this.textAreaValue.equals(initTextAreaValue)) {
            this.submitAction.accept(textAreaValue);
        }
    }

}
