package com.qnxy.window;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * 表格操作自定义
 *
 * @author Qnxy
 */
public abstract class TableCellOperate<T, TYPE extends Enum<TYPE> & TableCellOperate.ActionName> extends DefaultCellEditor implements TableCellRenderer {


    private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));

    private List<T> dataList;
    private T data;
    private TYPE[] actionTypes;

    public TableCellOperate(TYPE[] actionTypes) {
        super(new JTextField());
        this.actionTypes = actionTypes;

        setClickCountToStart(1);

        for (TYPE actionType : actionTypes) {
            final JButton b = new JButton(actionType.getActionName());
            b.setPreferredSize(new Dimension(69, 26));

            b.addActionListener(e -> this.execActionByType(actionType, data));
            panel.add(b);
        }

    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        data = dataList.get(row);
        return panel;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return panel;
    }


    public void setData(List<T> records) {
        dataList = records;
    }

    public int getWidth() {
        return actionTypes.length * 55;
    }

    public abstract void execActionByType(TYPE actionType, T data);


    public interface ActionName {

        String getActionName();

    }

}
