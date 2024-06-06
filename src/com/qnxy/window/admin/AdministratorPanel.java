package com.qnxy.window.admin;

import com.qnxy.common.data.PageInfo;
import com.qnxy.common.data.entity.CarInformation;
import com.qnxy.service.AdminTableInfoService;
import com.qnxy.window.ChildPanelSupport;
import com.qnxy.window.QuickListenerAdder;
import com.qnxy.window.SetInputValueDocumentListener;
import com.qnxy.window.TableCellOperate;
import com.qnxy.window.common.LogoutPanel;
import com.qnxy.window.common.RentalTableDetailsDialog;
import com.qnxy.window.common.TablePanel;
import com.qnxy.window.common.TablePanel.NameAndValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * 管理员窗口
 *
 * @author Qnxy
 */
public final class AdministratorPanel extends ChildPanelSupport
        implements BiFunction<Integer, TablePanel.DataInitType, PageInfo<CarInformation>> {

    private final AdminTableInfoService adminTableInfoService = new AdminTableInfoService();

    // 当前面板表格表头及对应值获取方式数据
    private final List<NameAndValue<CarInformation>> tableHeaderDataList = new ArrayList<NameAndValue<CarInformation>>() {{
        add(NameAndValue.of("编号", CarInformation::getId));
        add(NameAndValue.of("车型", CarInformation::getCarType));
        add(NameAndValue.of("车主", CarInformation::getCarRenters));
        add(NameAndValue.of("价格(元/天)", CarInformation::getPrice));
        add(NameAndValue.of("颜色", CarInformation::getColor));
        add(NameAndValue.of("是否被租用", it -> it.getUid() != null ? "是" : "否"));
        add(NameAndValue.of("租用的用户", CarInformation::getUid));
        add(NameAndValue.of("操作", AdminTableOpt::new));
    }};
    // 当前面板表格
    private TablePanel<CarInformation> dataTablePanel;


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
        dataTablePanel = new TablePanel<>(tableHeaderDataList, this);

        add(dataTablePanel, BorderLayout.CENTER);

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
        textField.setPreferredSize(new Dimension(230, 30));
        textField.getDocument().addDocumentListener((SetInputValueDocumentListener) inputValue -> this.inputValue = inputValue);

        optionPanel.add(textField);

        new QuickListenerAdder(optionPanel)
                .add(new JButton("查询"), e -> this.dataTablePanel.refreshTableData(getData(1)))
                .add(new JButton("汽车信息录入"), e -> carInformationEntryAction())
                .add(new JButton("清空/刷新"), e -> this.clearAndRefreshAction(textField));

        panel.add(optionPanel, BorderLayout.EAST);
        return panel;
    }

    /**
     * 重置输入内容 刷新列表数据
     */
    private void clearAndRefreshAction(JTextField textField) {
        textField.setText("");
        this.dataTablePanel.refreshTableData(getData(1));
    }

    /**
     * 汽车信息录入事件
     */
    private void carInformationEntryAction() {

        new InformationEntryDialog(
                (Frame) getRootPane().getParent(),
                it -> {

                    if (adminTableInfoService.addAdminTableInfo(it)) {
                        JOptionPane.showMessageDialog(this, "录入成功");
                        AdministratorPanel.this.dataTablePanel.refreshTableData(getData(1));
                        return true;
                    } else {
                        JOptionPane.showMessageDialog(this, "录入失败");
                        return false;
                    }
                }
        );

    }

    /**
     * 表格数据更新事件
     */
    @Override
    public PageInfo<CarInformation> apply(Integer currentPage, TablePanel.DataInitType dataInitType) {

        switch (dataInitType) {
            case INIT:
                return getData(currentPage);
            case UP_PAGE:
                if (currentPage <= 1) {
                    JOptionPane.showMessageDialog(this, "已经是第一页了");
                    return null;
                } else {
                    return getData(currentPage - 1);
                }
            case NEXT_PAGE:
                return getData(currentPage + 1);
        }

        return null;
    }


    private PageInfo<CarInformation> getData(Integer currentPage) {

        Integer id = null;
        try {
            if (!(this.inputValue == null || this.inputValue.trim().isEmpty())) {
                id = Integer.parseInt(this.inputValue);
            }
        } catch (NumberFormatException e) {
            // tishi
        }
        PageInfo<CarInformation> carInformationPageInfo = this.adminTableInfoService.selectTableInfo(id, currentPage);

        if (carInformationPageInfo.getRecords().isEmpty()) {
            JOptionPane.showMessageDialog(this, "已经没有更多数据了!");
            return null;
        } else {
            return carInformationPageInfo;
        }
    }

    @RequiredArgsConstructor
    @Getter
    private enum AdminTableOptAction implements TableCellOperate.ActionName {
        UPDATE("更新"),
        DELETE("删除"),
        DETAILS("详情");

        private final String actionName;
    }

    private class AdminTableOpt extends TableCellOperate<CarInformation, AdminTableOptAction> {

        public AdminTableOpt() {
            super(AdminTableOptAction.values());
        }

        @Override
        public void execActionByType(AdminTableOptAction actionType, CarInformation data) {
            switch (actionType) {
                case DELETE:
                    JOptionPane.showMessageDialog(AdministratorPanel.this, actionType.getActionName() + "开发中");
                    break;
                case DETAILS:
                    new RentalTableDetailsDialog(((JFrame) getRootPane().getParent()), data.toString(), it -> System.out.println("输入内容为: \n" + it));
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
