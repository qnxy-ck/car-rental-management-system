package com.qnxy.window.admin;

import com.qnxy.common.data.PageInfo;
import com.qnxy.common.data.ui.AdminTableData;
import com.qnxy.window.ChildPanelSupport;
import com.qnxy.window.TablePanel;
import com.qnxy.window.TablePanel.NameAndValue;
import com.qnxy.window.login.LoginPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理员窗口
 *
 * @author Qnxy
 */
public final class AdministratorPanel extends ChildPanelSupport {

    private static final List<AdminTableData> userInfoList = new ArrayList<>();

    // TODO 2024-05-28 临时数据 后续删除
    static {
        for (int i = 0; i < 20; i++) {
            userInfoList.add(
                    new AdminTableData(
                            i,
                            "username" + i,
                            "carOwner" + i,
                            "price" + i,
                            "carColor" + i,
                            i % 2 == 0,
                            "leasedUser" + i
                    )
            );
        }

    }

    // 当前面板表格表头及对应值获取方式数据
    private final List<NameAndValue<AdminTableData>> tableHeaderDataList = new ArrayList<NameAndValue<AdminTableData>>() {{
        add(NameAndValue.of("编号", AdminTableData::getId));
        add(NameAndValue.of("车型", AdminTableData::getCarModel));
        add(NameAndValue.of("车主", AdminTableData::getCarOwner));
        add(NameAndValue.of("价格(元/天)", AdminTableData::getPrice));
        add(NameAndValue.of("颜色", AdminTableData::getCarColor));
        add(NameAndValue.of("是否被租用", it -> it.getLeased() ? "是" : "否"));
        add(NameAndValue.of("租用的用户", AdminTableData::getLeasedUser));
        add(NameAndValue.of("操作", it -> new AdminTableOpt()));
    }};
    // 当前面板表格
    private TablePanel<AdminTableData> userInfoTablePanel;


    @Override
    protected void initialization(ParentFrameScope parentFrameScope) {
        parentFrameScope.setWindowTitle("管理员窗口");
        // 设置父页面关闭按钮关闭行为
        parentFrameScope.setDefaultCloseOperation(CloseOperation.DO_NOTHING_ON_CLOSE);

        // 上下布局
        setLayout(new BorderLayout());

        add(topButtonPanel(), BorderLayout.NORTH);

        // 中间的表格
        userInfoTablePanel = new TablePanel<>(
                tableHeaderDataList,
                () -> new PageInfo<>(userInfoList, 1, 15, 100),
                () -> new PageInfo<>(userInfoList, 1, 15, 100),
                () -> new PageInfo<>(userInfoList, 2, 15, 100)
        );


        // TODO 2024-05-28  示例
        new Thread(() -> {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 数据块查询数据
            userInfoTablePanel.refreshTableData(new PageInfo<>(userInfoList, 2, 15, 100));

        }).start();

        add(userInfoTablePanel, BorderLayout.CENTER);

    }

    /**
     * 顶部按钮面板
     */
    private JPanel topButtonPanel() {
        final FlowLayout flowLayout = new FlowLayout(FlowLayout.RIGHT, 15, 20);
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(0, 70));

        // 退出按钮
        panel.add(topLogoutPanel(flowLayout), BorderLayout.WEST);

        // 其他操作
        final JPanel optionPanel = new JPanel(flowLayout);
        final JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 30));
        optionPanel.add(textField);
        optionPanel.add(new JButton("查询"));
        optionPanel.add(new JButton("汽车信息录入") {{
            addActionListener(e -> carInformationEntry());
        }});
        optionPanel.add(new JButton("刷新列表") {{
            addActionListener(e -> userInfoTablePanel.refreshTableData(
                    new PageInfo<>(userInfoList, 1, 10, 100)
            ));
        }});


        panel.add(optionPanel, BorderLayout.EAST);
        return panel;
    }

    /**
     * 汽车信息录入事件
     */
    private void carInformationEntry() {
        new InformationEntryDialog(
                ((Frame) getRootPane().getParent()),
                it -> {
                    JOptionPane.showMessageDialog(null, "录入成功");
                    return true;
                }
        );

    }

    private JPanel topLogoutPanel(FlowLayout flowLayout) {
        final JPanel logoutPanel = new JPanel(flowLayout);
        final JButton button = new JButton("退出登录");
        button.setBackground(new Color(255, 254, 240));
        button.addActionListener(e -> this.logoutAction());

        logoutPanel.add(button);
        return logoutPanel;
    }

    private void logoutAction() {
        final int dialogFlag = JOptionPane.showConfirmDialog(
                AdministratorPanel.this,
                "确定退出吗!",
                "是否确认退出",
                JOptionPane.YES_NO_OPTION
        );

        if (dialogFlag == JOptionPane.YES_OPTION) {
            removeThisAndAdd(new LoginPanel());
        }
    }

    private class AdminTableOpt extends AdminOptTableCellEditor<AdminTableData> {


        @Override
        public void updateAction(AdminTableData data) {
            new InformationEntryDialog(
                    ((Frame) getRootPane().getParent()),
                    data,
                    it -> {
                        JOptionPane.showMessageDialog(null, "更新成功");
                        return true;
                    }
            );
        }

        @Override
        public void deleteAction(AdminTableData data) {

        }


    }


}
