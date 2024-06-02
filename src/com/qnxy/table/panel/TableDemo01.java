package com.qnxy.table.panel;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * 普通简单表格
 * <p>
 * 最基础的使用方式
 * <p>
 * 只能简单示例使用
 * <p>
 * 正确使用方式应该使用 {@link TableModel} 方式
 *
 * @author Qnxy
 */
public class TableDemo01 extends JPanel {

    /**
     * 创建一个表格类
     */
    private final JTable table;

    /**
     * 表头信息
     */
    private final String[] tableHeaders = {"id", "username", "age", "birthday"};

    /**
     * 表数据信息(二维数组)
     * <p>
     * rowData[rowIndex] == 每一行数据
     * <p>
     * rowData[rowIndex][columnIndex] == 某一行的某一列数据
     */
    private final Object[][] rowData = {
            {1, "name1", "age1", "birthday1"},
            {2, "name1", "age1", "birthday1"},
            {3, "name1", "age1", "birthday1"},

    };

    public TableDemo01() {
        setLayout(new BorderLayout());
        setSize(new Dimension(1200, 700));


        // 初始化表格
        this.table = new JTable(
                rowData,
                tableHeaders
        );

        // 表格相关属性设置 ...
        table.setRowHeight(35);


        // JScrollPane 给表格增加滚动条, 并且使用 JScrollPane 会自动展示表头信息
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane);
    }
}
