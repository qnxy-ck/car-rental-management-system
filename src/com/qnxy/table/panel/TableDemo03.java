package com.qnxy.table.panel;

import com.qnxy.table.UserInfoDataInitUtils;
import com.qnxy.table.data.UserInfo;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * {@link TableModel} 方式使用表格
 * 进阶使用, 表格单元格中增加按钮组件
 *
 * @author Qnxy
 */
public class TableDemo03 extends JPanel {

    /**
     * 创建一个表格类
     */
    private final JTable table;

    /**
     * 表头信息
     */
    private final String[] tableHeaders = {"id", "username", "age", "birthday", "opt"};

    /**
     * 初始化表格数据
     */
    private final List<UserInfo> tableDataList = new ArrayList<>();

    public TableDemo03() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1200, 700));
        
        
        /*
           TableModel 方式与第一种普通方式的区别就是数据初始化的方式不同
           TableModel 可以让你对数据更灵活的初始化, 而不是使用二维数组的方式进行初始化, 二维数组无法高度自定义, 使用复杂
           不符合正常使用时的数据类型
           正常使用时, 查询数据库返回的数据通常是一个集合而不是一个二维数组, 如果使用二维数组的方式
           需要进行二次转换, 使用繁琐.
         */
        final DemoTableModel demoTableModel = new DemoTableModel();
        // 初始化表格
        this.table = new JTable(demoTableModel);

        // 表格相关属性设置 ...
        table.setRowHeight(35);

        // 添加自定义单元格
        // 使单元格中可以展示控件

        // 获取对列的操作
        final TableColumn tableColumn = table.getColumnModel()
                // 获取最后一列
                .getColumn(tableHeaders.length - 1);

        tableColumn.setCellRenderer(new TableCellOpt());
        tableColumn.setCellEditor(new TableCellOpt());


        // 创建表格后立即初始化数据
//        this.initTableData();

        // JScrollPane 给表格增加滚动条, 并且使用 JScrollPane 会自动展示表头信息
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        this.add(
                new JButton("点击加载数据") {{
                    addActionListener(e -> TableDemo03.this.initTableData());
                }},
                BorderLayout.SOUTH
        );
    }

    /**
     * 模拟数据库加载需要展示数据
     */
    private static List<UserInfo> loadDbUserInfoList() {
        return UserInfoDataInitUtils.userInfoList();
    }

    /**
     * 初始化表格数据
     */
    private void initTableData() {
        final List<UserInfo> userInfos = loadDbUserInfoList();
        this.tableDataList.clear();
        this.tableDataList.addAll(userInfos);

        // 使表格重新绘制, 根据最新的素具
        this.table.updateUI();
    }

    /**
     * 使用自定义 TableModel 时需要实现很多方法, 但是有些方法我们并不关心
     * 这是只有实现它的子类 {@link AbstractTableModel} (抽象类), 实现部分功能即可
     */
    private class DemoTableModel extends AbstractTableModel {

        /**
         * 构建表格时, 表格的行数
         */
        @Override
        public int getRowCount() {
            return TableDemo03.this.tableDataList.size();
        }

        /**
         * 构建表格时, 表格的列数量
         */
        @Override
        public int getColumnCount() {
            return TableDemo03.this.tableHeaders.length;
        }

        /**
         * 构建表格时 获取表格列名的方法  (表头名称)
         *
         * @param column 表格的那一列名称
         */
        @Override
        public String getColumnName(int column) {
            return TableDemo03.this.tableHeaders[column];
        }

        /**
         * 生成表格时表格的数据获取方式
         *
         * @param rowIndex    数据的第几行
         * @param columnIndex 某行的第几列
         * @return 实际要展示的数据
         */
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            final UserInfo userInfo = TableDemo03.this.tableDataList.get(rowIndex);

            return switch (columnIndex) {
                case 0 -> userInfo.getId();
                case 1 -> userInfo.getUsername();
                case 2 -> userInfo.getAge();
                case 3 ->
                    // LocalDate
                        userInfo.getBirthday();
                default -> null;
            };
        }

        /**
         * 单元格是否可编辑
         * 返回 false 不可编辑
         *
         * @param rowIndex    第几行
         * @param columnIndex 第几列
         */
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == tableHeaders.length - 1;
        }
    }


    /**
     * 自定义表格数据展示内容
     * 可以展示一个控件
     * 比如按钮, 输入框
     */
    private class TableCellOpt extends DefaultCellEditor implements TableCellRenderer {

        private final JPanel panel = new JPanel();


        public TableCellOpt() {
            super(new JTextField());
            initOptPanel();

            setClickCountToStart(1);

        }


        private void initOptPanel() {
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
            panel.setPreferredSize(new Dimension(100, 50));


            List.of("修改", "删除")
                    .forEach(name -> panel.add(
                                    new JButton(name) {{
                                        addActionListener(e -> showMessageDialog(TableDemo03.this, "你点击了 -> " + name));
                                    }}
                            )
                    );

        }


        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return panel;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return panel;
        }
    }

}

