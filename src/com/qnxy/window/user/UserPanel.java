package com.qnxy.window.user;

import com.qnxy.common.data.PageInfo;
import com.qnxy.common.data.entity.CarInformation;
import com.qnxy.service.UserTableInfoService;
import com.qnxy.window.ChildPanelSupport;
import com.qnxy.window.QuickListenerAdder;
import com.qnxy.window.SetInputValueDocumentListener;
import com.qnxy.window.TableCellOperate;
import com.qnxy.window.common.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * 用户管理面板
 *
 * @author Qnxy
 */
public final class UserPanel extends ChildPanelSupport
        implements BiFunction<Integer, TablePanel.DataInitType, PageInfo<CarInformation>> {
    private final UserTableInfoService userTableInfoService = new UserTableInfoService();
    // 当前面板表格表头及对应值获取方式数据
    private final List<NameAndValue<CarInformation>> tableHeaderDataList = new ArrayList<NameAndValue<CarInformation>>() {{
        add(NameAndValue.of("编号", CarInformation::getId));
        add(NameAndValue.of("车型", CarInformation::getCarType));
        add(NameAndValue.of("车主", CarInformation::getCarRenters));
        add(NameAndValue.of("价格(元/天)", CarInformation::getPrice));
        add(NameAndValue.of("颜色", CarInformation::getColor));
        add(NameAndValue.of("是否被租用", it -> it.getUid() != null ? "是" : "否"));

        add(NameAndValue.of("操作", UserTableOpt::new));
    }};

    // 默认全部
    private boolean allOrRent = true;
    private final Map<String, Boolean> map = new HashMap<String, Boolean>() {{
        put("全部", allOrRent);
        put("可租用", !allOrRent);
    }};

    private final Integer userId;
    private String inputValue = "";
    private RadioButtonGroup<Boolean> radioButtonGroup;
    private TablePanel<CarInformation> dataTablePanel;

    public UserPanel(Integer userId) {
        this.userId = userId;
    }

    @Override
    protected void initialization(ParentFrameScope parentFrameScope) {
        parentFrameScope.setWindowTitle("用户管理界面");
        parentFrameScope.setDefaultCloseOperation(CloseOperation.DO_NOTHING_ON_CLOSE);

        setLayout(new BorderLayout());

        add(topButtonPanel(), BorderLayout.NORTH);

        // 中间的表格
        dataTablePanel = new TablePanel<>(tableHeaderDataList, this);

        add(dataTablePanel, BorderLayout.CENTER);

    }

    private JPanel topButtonPanel() {
        final FlowLayout flowLayout = new FlowLayout(FlowLayout.RIGHT, 15, 20);
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(0, 70));

        // 退出按钮
        panel.add(new LogoutPanel(this), BorderLayout.WEST);


        // 其他操作
        final JPanel optionPanel = new JPanel(flowLayout);

        // 筛选条件(全部还是可租用)
        radioButtonGroup = new RadioButtonGroup<>(
                optionPanel,
                map,
                allOrRent,
                it -> {
                    allOrRent = it;
                    this.dataTablePanel.refreshTableData(
                            this.tableQueryAction(1, false)
                    );
                }
        );


        final JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(230, 30));
        textField.getDocument().addDocumentListener((SetInputValueDocumentListener) inputValue -> this.inputValue = inputValue);

        optionPanel.add(textField);

        new QuickListenerAdder(optionPanel)
                .add(new JButton("查询"), e -> this.queryAction())
                .add(new JButton("我的租车"), e -> this.myRentalCarAction())
                .add(new JButton("清空/刷新"), e -> this.clearAndRefreshAction(textField));

        panel.add(optionPanel, BorderLayout.EAST);
        return panel;
    }

    /**
     * 查询按钮事件
     */
    private void queryAction() {
        this.dataTablePanel.refreshTableData(
                this.tableQueryAction(1, false)
        );
    }

    /**
     * 我的租车事件
     */
    private void myRentalCarAction() {
        this.dataTablePanel.refreshTableData(
                this.tableQueryAction(1, true)
        );
    }

    /**
     * 列表数据加载 / 数据查询
     *
     * @param rent 是否为查看我的租车
     */
    private PageInfo<CarInformation> tableQueryAction(Integer currentPage, boolean rent) {
        Integer id = null;
        try {
            if (!(this.inputValue == null || this.inputValue.trim().isEmpty())) {
                id = Integer.parseInt(this.inputValue);
            }
        } catch (NumberFormatException e) {
            // ignore
        }

        // 查询数据库中数据
        return this.userTableInfoService.selectUserCarInfo(
                id,
                this.allOrRent,
                rent ? this.userId : null,
                currentPage
        );

    }

    /**
     * 重置输入内容和选项 刷新列表数据
     */
    private void clearAndRefreshAction(JTextField textField) {
        textField.setText("");
        this.radioButtonGroup.restoreSelection();

        this.dataTablePanel.refreshTableData(
                this.tableQueryAction(1, false)
        );
    }


    @Override
    public PageInfo<CarInformation> apply(Integer currentPage, TablePanel.DataInitType dataInitType) {
        switch (dataInitType) {
            case INIT:
                return this.tableQueryAction(1, false);
            case UP_PAGE:
                if (currentPage <= 1) {
                    JOptionPane.showMessageDialog(this, "已经是第一页了");
                    return null;
                } else {
                    return this.tableQueryAction(currentPage - 1, false);
                }
            case NEXT_PAGE:
                return this.tableQueryAction(currentPage + 1, false);
        }
        return null;
    }

    private class UserTableOpt extends TableCellOperate<CarInformation, UserTableOptAction> {

        public UserTableOpt() {
            super(UserTableOptAction.values());
        }


        @Override
        public void execActionByType(UserTableOptAction actionType, CarInformation data) {
            switch (actionType) {
                case DETAILS:
                    new RentalTableDetailsDialog(
                            (JFrame) getRootPane().getParent(),
                            data.getInformation()
                    );
                    break;
                case RENT:
                    UserTableInfoService.RentUpdate rentUpdate = UserPanel.this.userTableInfoService
                            .rent(data.getId(), userId);
                    switch (rentUpdate) {
                        case SUC:
                            JOptionPane.showMessageDialog(UserPanel.this, "租用成功");
                            UserPanel.this.queryAction();
                            return;
                        case ERR:
                            JOptionPane.showMessageDialog(UserPanel.this, "租用失败");
                            return;
                        case EXISTING:
                            JOptionPane.showMessageDialog(UserPanel.this, "已经被租用, 无法再次租用");
                    }

            }
        }

    }
}
