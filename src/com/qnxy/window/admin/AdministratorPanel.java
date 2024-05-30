package com.qnxy.window.admin;

import com.qnxy.common.data.PageInfo;
import com.qnxy.common.data.ui.AdminTableData;
import com.qnxy.window.*;
import com.qnxy.window.TablePanel.NameAndValue;

import javax.swing.*;
import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 管理员窗口
 *
 * @author Qnxy
 */
public final class AdministratorPanel extends ChildPanelSupport
        implements BiFunction<Integer, TablePanel.DataInitType, PageInfo<AdminTableData>> {

    private static final SecureRandom rand = new SecureRandom();
    private static final List<AdminTableData> userInfoList = getUserInfoList();

    private static List<AdminTableData> getUserInfoList() {
        return Stream.generate(() -> new AdminTableData(
                        0,
                        "carModel " + rand.nextBoolean(),
                        "carOwner",
                        "price",
                        "carColor",
                        false,
                        "leasedUser"
                ))
                .limit(16)
                .collect(Collectors.toList());

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
    private String inputValue = "";


    @Override
    protected void initialization(ParentFrameScope parentFrameScope) {
        parentFrameScope.setWindowTitle("管理员窗口");
        // 设置父页面关闭按钮关闭行为
        parentFrameScope.setDefaultCloseOperation(CloseOperation.DO_NOTHING_ON_CLOSE);

        // 上下布局
        setLayout(new BorderLayout());

        add(topButtonPanel(), BorderLayout.NORTH);

        // 中间的表格
        userInfoTablePanel = new TablePanel<>(tableHeaderDataList, this);

        add(userInfoTablePanel, BorderLayout.CENTER);

    }

    /**
     * 顶部按钮面板
     */
    private JPanel topButtonPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(0, 70));

        // 退出按钮
        panel.add(new LogoutPanel(this), BorderLayout.WEST);

        // 其他操作
        final JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));

        // 查询
        final JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 30));
        textField.getDocument().addDocumentListener((SetInputValueDocumentListener) inputValue -> this.inputValue = inputValue);

        optionPanel.add(textField);

        new QuickListenerAdder(optionPanel)
                .add(new JButton("查询"), e -> JOptionPane.showMessageDialog(this, "查询内容为: " + this.inputValue + "\n\n查询功能实现中\n"))
                .add(new JButton("汽车信息录入"), e -> carInformationEntry())
                .add(new JButton("刷新列表"), e -> JOptionPane.showMessageDialog(this, "刷新列表未实现"));

        panel.add(optionPanel, BorderLayout.EAST);
        return panel;
    }

    /**
     * 汽车信息录入事件
     */
    private void carInformationEntry() {
        new InformationEntryDialog(
                (Frame) getRootPane().getParent(),
                it -> {
                    JOptionPane.showMessageDialog(this, "录入成功");
                    return true;
                }
        );

    }


    /**
     * 表格数据更新事件
     */
    @Override
    public PageInfo<AdminTableData> apply(Integer currentPage, TablePanel.DataInitType dataInitType) {

        switch (dataInitType) {
            case INIT:
                return new PageInfo<>(userInfoList, 1, 100);
            case UP_PAGE:
                if (currentPage <= 1) {
                    JOptionPane.showMessageDialog(this, "已经是第一页了");
                    return null;
                } else {
                    return new PageInfo<>(getUserInfoList(), currentPage - 1, 100);
                }
            case NEXT_PAGE:
                return new PageInfo<>(getUserInfoList(), currentPage + 1, 100);
        }

        return null;
    }


    private enum AdminTableOptAction implements TableCellOperate.ActionName {
        UPDATE("更新"),
        DELETE("删除"),
        DETAILS("详情"),

        ;
        private final String name;

        AdminTableOptAction(String name) {
            this.name = name;
        }

        @Override
        public String getActionName() {
            return name;
        }
    }

    private class AdminTableOpt extends TableCellOperate<AdminTableData, AdminTableOptAction> {

        public AdminTableOpt() {
            super(AdminTableOptAction.values());
        }

        @Override
        public void execActionByType(AdminTableOptAction actionType, AdminTableData data) {
            switch (actionType) {
                case DELETE:
                case DETAILS:
                    JOptionPane.showMessageDialog(AdministratorPanel.this, actionType.getActionName() + "开发中");
                    break;
                case UPDATE:
                    new InformationEntryDialog(
                            ((Frame) getRootPane().getParent()),
                            data,
                            it -> {
                                JOptionPane.showMessageDialog(null, "更新成功");
                                return true;
                            }
                    );
            }

        }
    }

}
