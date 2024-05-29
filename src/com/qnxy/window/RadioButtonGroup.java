package com.qnxy.window;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 快速创建单选按钮组
 *
 * @author Qnxy
 */
public final class RadioButtonGroup<V> {

    private final Map<String, V> btnNameAndValueMap;
    private final JPanel parentPanel;
    private final Consumer<V> selectedAction;
    private final V defaultSelected;

    private final ButtonGroup buttonGroup = new ButtonGroup();
    private JRadioButton defaultRadioButton;

    /**
     * 创建单选按钮组
     *
     * @param parentPanel        单选按钮需要放置的面板
     *                           在创建完成后会自动将单选按钮添加到该面板
     * @param btnNameAndValueMap 单选按钮信息
     *                           k: 按钮名称
     *                           v: 按钮实际的值
     *                           该 map 中 key 和 value 都不应该重复, 如果 value 重复并且默认选中的是重复项, 那么会将第一个设置为选中
     * @param defaultSelected    默认被选中的按钮是哪个
     * @param selectedAction     按钮点击时的动作
     *                           做点什么
     */
    public RadioButtonGroup(JPanel parentPanel, Map<String, V> btnNameAndValueMap, V defaultSelected, Consumer<V> selectedAction) {
        this.btnNameAndValueMap = btnNameAndValueMap;
        this.parentPanel = parentPanel;
        this.defaultSelected = defaultSelected;
        this.selectedAction = selectedAction;

        this.btnNameAndValueMap.forEach(this::initRadioBtn);
    }

    /**
     * 恢复默认选中的按钮
     */
    public void restoreSelection() {
        Optional.ofNullable(this.defaultRadioButton)
                .ifPresent(radioButton -> radioButton.setSelected(true));
    }

    private void initRadioBtn(String buttonName, V v) {
        final JRadioButton radioButton = new JRadioButton(buttonName);
        radioButton.addActionListener(radioButtonAction());

        if (v.equals(defaultSelected)) {
            radioButton.setSelected(true);
            this.defaultRadioButton = radioButton;
        }

        buttonGroup.add(radioButton);
        parentPanel.add(radioButton);
    }

    private ActionListener radioButtonAction() {
        return e -> {
            final String keyVal = e.getActionCommand();
            final V v = this.btnNameAndValueMap.get(keyVal);
            this.selectedAction.accept(v);
        };
    }
}
