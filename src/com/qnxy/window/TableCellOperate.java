package com.qnxy.window;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * 表格操作自定义
 * <p>
 * 如果表格需要加入按钮, 则在表格表头中使用该类
 *
 * @author Qnxy
 */
public abstract class TableCellOperate<T, TYPE extends Enum<TYPE> & ActionName>
        extends DefaultCellEditor
        implements TableCellRenderer {


    private final TYPE[] actionTypes;

    public TableCellOperate(TYPE[] actionTypes) {
        super(new JTextField());
        this.actionTypes = actionTypes;

        setClickCountToStart(1);
    }

    private JPanel createPanel(T data) {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        for (TYPE actionType : actionTypes) {
            final JButton b = new JButton(actionType.getActionName());
            b.setPreferredSize(new Dimension(69, 26));

            if (data != null) {
                b.addActionListener(e -> this.execActionByType(actionType, data));
            }

            panel.add(b);
        }
        return panel;
    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //noinspection unchecked
        return createPanel((T) value);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return createPanel(null);
    }

    public int getWidth() {
        return actionTypes.length * 55;
    }

    /**
     * 当点击对应事件按钮时, 将调用该方法
     *
     * @param actionType 响应的事件类型
     * @param data       响应的数据
     */
    public abstract void execActionByType(TYPE actionType, T data);


}
