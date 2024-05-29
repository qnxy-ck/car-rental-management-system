package com.qnxy.window.admin;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * @author Qnxy
 */
public abstract class AdminOptTableCellEditor<T> extends DefaultCellEditor implements TableCellRenderer {


    private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));

    private List<T> dataList;
    private T data;

    public AdminOptTableCellEditor() {
        super(new JTextField());

        setClickCountToStart(1);

        panel.add(new JButton("编辑") {{
            addActionListener(e -> updateAction(data));
            setPreferredSize(new Dimension(70, 26));
        }});

        panel.add(new JButton("删除") {{
            addActionListener(e -> deleteAction(data));
            setPreferredSize(new Dimension(70, 26));
        }});
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

    public abstract void updateAction(T data);

    public abstract void deleteAction(T data);

}
