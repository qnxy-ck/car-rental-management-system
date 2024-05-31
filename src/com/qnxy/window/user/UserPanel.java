package com.qnxy.window.user;

import com.qnxy.common.data.PageInfo;
import com.qnxy.common.data.ui.RentalTableData;
import com.qnxy.window.ChildPanelSupport;
import com.qnxy.window.QuickListenerAdder;
import com.qnxy.window.SetInputValueDocumentListener;
import com.qnxy.window.TableCellOperate;
import com.qnxy.window.common.LogoutPanel;
import com.qnxy.window.common.RadioButtonGroup;
import com.qnxy.window.common.RentalTableDetailsDialog;
import com.qnxy.window.common.TablePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static com.qnxy.window.TestSource.getUserInfoList;

/**
 * 用户管理面板
 *
 * @author Qnxy
 */
public final class UserPanel extends ChildPanelSupport
        implements BiFunction<Integer, TablePanel.DataInitType, PageInfo<RentalTableData>> {
    // 当前面板表格表头及对应值获取方式数据
    private final List<TablePanel.NameAndValue<RentalTableData>> tableHeaderDataList = new ArrayList<TablePanel.NameAndValue<RentalTableData>>() {{
        add(TablePanel.NameAndValue.of("编号", RentalTableData::getId));
        add(TablePanel.NameAndValue.of("车型", RentalTableData::getCarModel));
        add(TablePanel.NameAndValue.of("车主", RentalTableData::getCarOwner));
        add(TablePanel.NameAndValue.of("价格(元/天)", RentalTableData::getPrice));
        add(TablePanel.NameAndValue.of("颜色", RentalTableData::getCarColor));
        add(TablePanel.NameAndValue.of("是否被租用", it -> it.getLeased() ? "是" : "否"));
        add(TablePanel.NameAndValue.of("操作", it -> new UserTableOpt()));
    }};

    private final Map<String, Boolean> map = new HashMap<String, Boolean>() {{
        put("全部", true);
        put("可租用", false);
    }};

    private boolean allOrRent = true;
    private String inputValue = "";
    private TablePanel<RentalTableData> userInfoTablePanel;

    @Override
    protected void initialization(ParentFrameScope parentFrameScope) {
        parentFrameScope.setWindowTitle("用户管理界面");
        parentFrameScope.setDefaultCloseOperation(CloseOperation.DO_NOTHING_ON_CLOSE);

        setLayout(new BorderLayout());

        add(topButtonPanel(), BorderLayout.NORTH);

        // 中间的表格
        userInfoTablePanel = new TablePanel<>(tableHeaderDataList, this);

        add(userInfoTablePanel, BorderLayout.CENTER);

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
        new RadioButtonGroup<>(
                optionPanel,
                map,
                allOrRent,
                it -> allOrRent = it
        );


        final JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 30));
        textField.getDocument().addDocumentListener((SetInputValueDocumentListener) inputValue -> this.inputValue = inputValue);

        optionPanel.add(textField);

        new QuickListenerAdder(optionPanel)
                .add(new JButton("查询"), e -> JOptionPane.showMessageDialog(this, "查询内容为: " + this.inputValue + "\n\n查询功能实现中\n"))
                .add(new JButton("我的租车"), e -> JOptionPane.showMessageDialog(this, "我的租车查询功能实现中\n"))
                .add(new JButton("刷新列表"), e -> JOptionPane.showMessageDialog(this, "刷新列表未实现"));

        panel.add(optionPanel, BorderLayout.EAST);
        return panel;
    }

    @Override
    public PageInfo<RentalTableData> apply(Integer integer, TablePanel.DataInitType dataInitType) {
        return new PageInfo<>(getUserInfoList(), 1, 200);
    }

    private enum UserTableOptAction implements TableCellOperate.ActionName {
        RENT("租用"),
        DETAILS("详情");
        private final String name;

        UserTableOptAction(String name) {
            this.name = name;
        }

        @Override
        public String getActionName() {
            return name;
        }
    }

    private class UserTableOpt extends TableCellOperate<RentalTableData, UserTableOptAction> {

        public UserTableOpt() {
            super(UserTableOptAction.values());
        }


        @Override
        public void execActionByType(UserTableOptAction actionType, RentalTableData data) {
            switch (actionType) {
                case DETAILS:
                    new RentalTableDetailsDialog(
                            ((JFrame) getRootPane().getParent()),
                            data.toString()
                    );
                    break;
                case RENT:
                    JOptionPane.showMessageDialog(UserPanel.this, actionType.getActionName() + "开发中");
            }
        }

    }
}
