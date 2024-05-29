package com.qnxy.window.admin;

import com.qnxy.common.data.ui.AdminTableData;
import com.qnxy.window.LabelTextField;
import com.qnxy.window.RadioButtonGroup;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

/**
 * 车辆信息录入界面
 *
 * @author Qnxy
 */
public final class InformationEntryDialog extends JDialog {

    /**
     * 租用单选按钮信息
     */
    public static final LinkedHashMap<String, Boolean> LEASED_STATUS_MAP = new LinkedHashMap<String, Boolean>() {{
        put("是", true);
        put("否", false);
    }};

    /**
     * 当前页面展示数据
     */
    private final AdminTableData adminTableData;
    private final List<LabelTextField> labelTextFieldList = new ArrayList<>();

    /**
     * 是否为更新方式
     * 用于判断是否需要展示 车辆编号/窗口高度/窗口标题等
     */
    private final boolean isUpdate;

    /**
     * 提交的监听
     */
    private final Function<AdminTableData, Boolean> submitAction;

    private RadioButtonGroup<Boolean> radioButtonGroup;


    /**
     * 用于创建添加
     */
    public InformationEntryDialog(Frame rootFrame, Function<AdminTableData, Boolean> submitAction) {
        this(rootFrame, null, submitAction);
    }

    /**
     * 用于创建更新
     *
     * @param adminTableData 回显的数据
     */
    public InformationEntryDialog(Frame rootFrame, AdminTableData adminTableData, Function<AdminTableData, Boolean> submitAction) {
        super(rootFrame);
        this.submitAction = submitAction;

        // 更新时回显数据不能为空, 如果为空则为添加
        if (adminTableData == null) {
            this.isUpdate = false;
            this.adminTableData = new AdminTableData();
            // 设置默认为未被租用
            this.adminTableData.setLeased(false);
        } else {
            this.isUpdate = true;
            this.adminTableData = adminTableData;
        }


        initDialog();
    }

    /**
     * 初始化窗口信息
     */
    private void initDialog() {
        setLayout(new BorderLayout());
        // 设置当前窗口大小
        setSize(new Dimension(400, isUpdate ? 520 : 480));
        // 设置窗口居中
        setLocationRelativeTo(null);
        // 窗口大小不可修改
        setResizable(false);

        setTitle(isUpdate ? "车辆信息修改页面" : "车辆信息录入界面");

        // 在当前窗口打开时, 无法操作其他窗口
        // false 则相反
        setModal(true);


        add(topInputPanel(), BorderLayout.NORTH);
        add(bottomButtonPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }


    /**
     * 输入框信息集合
     * 如果为空则添加
     */
    private List<LabelTextField> labelTextFieldInfo() {
        if (!labelTextFieldList.isEmpty()) {
            return labelTextFieldList;
        }

        // 根据输入条件判断是否展示序号
        if (isUpdate) {
            // 修改时序号不可编辑
            labelTextFieldList.add(new LabelTextField("序      号:", adminTableData.getId().toString(), false, null));
        }

        labelTextFieldList.add(new LabelTextField("车      型:", adminTableData.getCarModel(), adminTableData::setCarModel));
        labelTextFieldList.add(new LabelTextField("车      主:", adminTableData.getCarOwner(), adminTableData::setCarOwner));
        labelTextFieldList.add(new LabelTextField("价格(元/天):", adminTableData.getPrice(), adminTableData::setPrice));
        labelTextFieldList.add(new LabelTextField("颜      色:", adminTableData.getCarColor(), adminTableData::setCarColor));

        return labelTextFieldList;
    }

    /**
     * 信息录入输入框
     */
    private JPanel topInputPanel() {
        final FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 20, 30);

        return new JPanel(flowLayout) {{
            setPreferredSize(new Dimension(400, isUpdate ? 390 : 350));

            // 将输入框添加到面板
            labelTextFieldInfo().forEach(this::add);

            add(new JLabel("是否已租用:"));
            // 是否已租用单选按钮
            radioButtonGroup = new RadioButtonGroup<>(
                    this,
                    LEASED_STATUS_MAP,
                    adminTableData.getLeased(),
                    adminTableData::setLeased
            );


//            final ButtonGroup buttonGroup = new ButtonGroup();
//
//            // 初始化单选框
//            final List<JRadioButton> radioButtonList = Stream.of("否", "是")
//                    .map(JRadioButton::new)
//                    .peek(it -> {
//                        // 为单选框设置事件, 如果选择内容为 是 则是否被租用 为true
//                        it.addActionListener(e -> adminTableData.setLeased(e.getActionCommand().equals("是")));
//                        buttonGroup.add(it);
//                        add(it);
//                    })
//                    .collect(Collectors.toList());
//
//            // 设置第一个单选框为选中状态
//            final JRadioButton jRadioButton = radioButtonList.get(0);
//
//            // radioButtonList 第一个元素为 否
//            // 设置 adminTableData  是否被租用 默认为 false(未租用)
//            // Tips: 如果设置为 true 和默认选择按钮不对应
//            adminTableData.setLeased(false);
//            buttonGroup.setSelected(jRadioButton.getModel(), true);
        }};
    }

    /**
     * 底部按钮面板
     */
    private JPanel bottomButtonPanel() {
        return new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0)) {{
            setPreferredSize(new Dimension(400, 50));

            add(new JButton("提交") {{
                addActionListener(e -> {
                    if (submitAction.apply(adminTableData)) {
                        InformationEntryDialog.this.dispose();
                    }
                });
            }});

            add(new JButton("取消") {{
                addActionListener(e -> InformationEntryDialog.this.dispose());
            }});

            add(new JButton("重置") {{
                addActionListener(e -> {
                    labelTextFieldInfo().forEach(it -> {
                        if (isUpdate) {
                            // 更新时重置输入框内容
                            it.restoreDefaultText();
                        } else {
                            // 新增时清空输入框
                            it.clearFieldValue();
                        }
                    });
                    radioButtonGroup.restoreSelection();
                });
            }});
        }};
    }


}
