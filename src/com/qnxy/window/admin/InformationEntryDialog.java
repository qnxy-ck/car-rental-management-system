package com.qnxy.window.admin;

import com.qnxy.common.data.ui.RentalTableData;
import com.qnxy.window.QuickListenerAdder;
import com.qnxy.window.common.LabelTextField;
import com.qnxy.window.common.RadioButtonGroup;

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
final class InformationEntryDialog extends JDialog {

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
    private final RentalTableData rentalTableData;
    private final List<LabelTextField> labelTextFieldList = new ArrayList<>();

    /**
     * 是否为更新方式
     * 用于判断是否需要展示 车辆编号/窗口高度/窗口标题等
     */
    private final boolean isUpdate;

    /**
     * 提交的监听
     */
    private final Function<RentalTableData, Boolean> submitAction;

    private RadioButtonGroup<Boolean> radioButtonGroup;


    /**
     * 用于创建添加
     */
    public InformationEntryDialog(Frame rootFrame, Function<RentalTableData, Boolean> submitAction) {
        this(rootFrame, null, submitAction);
    }

    /**
     * 用于创建更新
     *
     * @param rentalTableData 回显的数据
     */
    public InformationEntryDialog(Frame rootFrame, RentalTableData rentalTableData, Function<RentalTableData, Boolean> submitAction) {
        super(rootFrame);
        this.submitAction = submitAction;

        // 更新时回显数据不能为空, 如果为空则为添加
        if (rentalTableData == null) {
            this.isUpdate = false;
            this.rentalTableData = new RentalTableData();
            // 设置默认为未被租用
            this.rentalTableData.setLeased(false);
        } else {
            this.isUpdate = true;
            this.rentalTableData = rentalTableData;
        }


        initDialog();
    }

    /**
     * 初始化窗口信息
     */
    private void initDialog() {
        setLayout(new BorderLayout());
        // 设置当前窗口大小
        setSize(new Dimension(400, getInputFieldHeight() + 170));
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
     * 获取输入框的总高度
     */
    private int getInputFieldHeight() {
        return this.labelTextFieldInfo().size() * 70;
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
            labelTextFieldList.add(new LabelTextField("序      号:", rentalTableData.getId().toString(), false, null));
        }

        labelTextFieldList.add(new LabelTextField("车      型:", rentalTableData.getCarModel(), rentalTableData::setCarModel));
        labelTextFieldList.add(new LabelTextField("车      主:", rentalTableData.getCarOwner(), rentalTableData::setCarOwner));
        labelTextFieldList.add(new LabelTextField("价格(元/天):", rentalTableData.getPrice(), rentalTableData::setPrice));
        labelTextFieldList.add(new LabelTextField("颜      色:", rentalTableData.getCarColor(), rentalTableData::setCarColor));

        return labelTextFieldList;
    }

    /**
     * 信息录入输入框
     */
    private JPanel topInputPanel() {
        final FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 20, 30);

        return new JPanel(flowLayout) {{
            final List<LabelTextField> labelTextFields = labelTextFieldInfo();

            setPreferredSize(new Dimension(400, getInputFieldHeight() + 60));

            // 将输入框添加到面板
            labelTextFields.forEach(this::add);

            add(new JLabel("是否已租用:"));
            // 是否已租用单选按钮
            radioButtonGroup = new RadioButtonGroup<>(
                    this,
                    LEASED_STATUS_MAP,
                    rentalTableData.getLeased(),
                    rentalTableData::setLeased
            );
        }};
    }

    /**
     * 底部按钮面板
     */
    private JPanel bottomButtonPanel() {
        return new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0)) {{
            setPreferredSize(new Dimension(400, 50));

            new QuickListenerAdder(this)
                    .add(new JButton("提交"), e -> {
                        if (submitAction.apply(rentalTableData)) {
                            InformationEntryDialog.this.dispose();
                        }
                    })
                    .add(new JButton("取消"), e -> InformationEntryDialog.this.dispose())
                    .add(new JButton("重置"), e -> {
                        labelTextFieldInfo().forEach(LabelTextField::restoreDefaultText);
                        radioButtonGroup.restoreSelection();
                    });
        }};
    }

}
