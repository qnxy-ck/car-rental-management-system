package com.qnxy.window.admin;

import com.qnxy.window.ChildPanelSupport;
import com.qnxy.window.login.LoginPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 管理员窗口
 *
 * @author Qnxy
 */
public class AdministratorPanel extends ChildPanelSupport {

    private static final String[] columnNames = {"编号", "车型", "车主", "价格(元/天)", "颜色", "是否被租用", "租用的用户", "操作"};

    @Override
    protected void initialization(ParentFrameScope parentFrameScope) {
        parentFrameScope.setDefaultCloseOperation(CloseOperation.DO_NOTHING_ON_CLOSE);

        setLayout(new BorderLayout());

        final FlowLayout flowLayout = new FlowLayout(FlowLayout.RIGHT, 15, 20);
        
        add(topPanel(flowLayout), BorderLayout.NORTH);
        add(builderTable(), BorderLayout.CENTER);
        add(bottomPagePanel(flowLayout), BorderLayout.SOUTH);

    }

   

    private JScrollPane builderTable() {
        final Object[][] values = new Object[100][columnNames.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = new Object[]{i, "轿车" + i, "ck" + i, "800", "红色" + i, "否", "", ""};
        }

        final JTable table = new JTable(values, columnNames);

        DefaultTableModel defaultTableModel = new DefaultTableModel(values, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(defaultTableModel);


        table.getColumnModel()
                .getColumn(columnNames.length - 1)
                .setCellRenderer((table1, value, isSelected, hasFocus, row, column) -> new JButton("编辑/修改") {{
                    addActionListener(e -> {
                        JOptionPane.showMessageDialog(AdministratorPanel.this, "哈哈哈哈哈哈哈哈哈哈");

                    });
                }});


        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1000, 500));
        return scrollPane;
    }

    private JPanel bottomPagePanel(FlowLayout flowLayout) {
        final JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(flowLayout);
        bottomPanel.setPreferredSize(new Dimension(0, 70));


        bottomPanel.add(new JButton("上一页"));
        bottomPanel.add(new JButton("下一页"));

        bottomPanel.add(new JLabel("跳转到:"));


        final JTextField jumpTextField = new JTextField();
        jumpTextField.setPreferredSize(new Dimension(50, 30));
        jumpTextField.setToolTipText("输入后敲击回车跳转");
        jumpTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    JOptionPane.showMessageDialog(AdministratorPanel.this, "你输入的是: " + jumpTextField.getText() + ". \n\n但是功能尚未实现!!!\n");
                }
            }
        });
        bottomPanel.add(jumpTextField);


        bottomPanel.add(new JLabel("当前页: 1"));
        bottomPanel.add(new JLabel("当前页条数: 10"));
        bottomPanel.add(new JLabel("总页数: 113"));

        return bottomPanel;
    }

    private JPanel topPanel(FlowLayout flowLayout) {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(0, 60));

        // 退出按钮
        panel.add(topLogoutPanel(flowLayout), BorderLayout.WEST);

        // 其他操作
        final JPanel optionPanel = new JPanel(flowLayout);
        final JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 30));
        optionPanel.add(textField);
        optionPanel.add(new JButton("查询"));
        optionPanel.add(new JButton("汽车信息录入"));
        optionPanel.add(new JButton("刷新列表"));


        panel.add(optionPanel, BorderLayout.EAST);
        return panel;
    }
    private JPanel topLogoutPanel(FlowLayout flowLayout) {
        final JPanel logoutPanel = new JPanel(flowLayout);
        final JButton button = new JButton("退出登录");
        button.setBackground(new Color(255, 254, 240));
        button.addActionListener(e -> {
            final int dialogFlag = JOptionPane.showConfirmDialog(
                    AdministratorPanel.this,
                    "确定退出吗!",
                    "是否确认退出",
                    JOptionPane.YES_NO_OPTION
            );

            if (dialogFlag == JOptionPane.YES_OPTION) {
                super.removeThisAndAdd(new LoginPanel());
            }
        });

        logoutPanel.add(button);
        return logoutPanel;
    }
   
}
