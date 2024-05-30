package com.qnxy.window;

import com.qnxy.common.data.PageInfo;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 自定义表格
 *
 * @author Qnxy
 */
public final class TablePanel<T> extends JPanel {

    /**
     * 表头信息
     */
    private final List<NameAndValue<T>> tableHeaderDataList;
    private final JTable table = new JTable();

    private final Map<JLabel, BiConsumer<JLabel, PageInfo<T>>> bottomLabelJLabelMap = new LinkedHashMap<JLabel, BiConsumer<JLabel, PageInfo<T>>>() {{
        put(new JLabel("当前页: 1"), (label, pageInfo) -> label.setText("当前页: " + pageInfo.getCurrentPage()));
        put(new JLabel("当前页数量: 0"), (label, pageInfo) -> label.setText("当前页数量: " + pageInfo.getPageSize()));
        put(new JLabel("总页数: 0"), (label, pageInfo) -> label.setText("总页数: " + pageInfo.getTotal()));
    }};

    private final BiFunction<Integer, DataInitType, PageInfo<T>> dataGetFunc;

    /**
     * 表格数据
     */
    private PageInfo<T> pageInfo;

    /**
     * 创建一个自定义表格
     *
     * @param tableHeaderDataList 当前表格表头信息和初始化数据方式
     */
    public TablePanel(List<NameAndValue<T>> tableHeaderDataList, BiFunction<Integer, DataInitType, PageInfo<T>> dataGetFunc) {
        this.tableHeaderDataList = tableHeaderDataList;
        this.dataGetFunc = dataGetFunc;

        initTablePanel();
        invokeDataGetFun(DataInitType.INIT);
    }


    private void initTablePanel() {
        // 设置自定义 tableModel
        table.setModel(new TablePanelModel());
        // 设置表格为单行选中
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // 设置表格行高
        table.setRowHeight(30);

        // 设置表格内容居中
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, r);

        // 设置表头不可拖动
        table.getTableHeader().setReorderingAllowed(false);

        // 将表格设置到当前 JScrollPane 中
        final JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1200, 500));

        setLayout(new BorderLayout());

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPagePanel(), BorderLayout.SOUTH);
    }

    /**
     * 底部信息按钮
     */
    private JPanel bottomPagePanel() {
        return new JPanel() {{
            setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 20));
            setPreferredSize(new Dimension(0, 60));

            new QuickListenerAdder(this)
                    .add(new JButton("上一页"), e -> invokeDataGetFun(DataInitType.UP_PAGE))
                    .add(new JButton("下一页"), e -> invokeDataGetFun(DataInitType.NEXT_PAGE));

            add(new JLabel("跳转到:"));
            add(jumpTextField());

            bottomLabelJLabelMap.keySet().forEach(this::add);
        }};
    }

    private JTextField jumpTextField() {
        final JTextField jumpTextField = new JTextField();
        jumpTextField.setPreferredSize(new Dimension(50, 30));
        jumpTextField.setToolTipText("输入后敲击回车跳转");
        jumpTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    JOptionPane.showMessageDialog(
                            TablePanel.this,
                            "你输入的是: " + jumpTextField.getText() + ". \n\n但是功能尚未实现!!!\n"
                    );
                }
            }
        });
        return jumpTextField;
    }

    private void invokeDataGetFun(DataInitType dataInitType) {
        final Integer currentPage = Optional.ofNullable(this.pageInfo)
                .map(PageInfo::getCurrentPage)
                .orElse(0);

        final PageInfo<T> p = this.dataGetFunc.apply(currentPage, dataInitType);
        if (p != null) {
            this.pageInfo = p;
            refreshTableData(p);
        }
    }

    /**
     * 刷新当前表格数据信息
     */
    public void refreshTableData(PageInfo<T> pageInfo) {
        this.bottomLabelJLabelMap.forEach((label, consumer) -> consumer.accept(label, pageInfo));
        table.updateUI();
    }

    /**
     * 数据初始化以及获取方式
     */
    public enum DataInitType {
        /**
         * 数据初始化
         */
        INIT,

        /**
         * 上一页数据
         */
        UP_PAGE,

        /**
         * 下一页数据
         */
        NEXT_PAGE

    }

    /**
     * 表格表头数据和对应数据初始化方式
     *
     * @param <T>
     */
    public static class NameAndValue<T> {
        private final String tableName;
        private final Function<T, Object> tableValueFunction;

        private NameAndValue(String tableName, Function<T, Object> tableValueFunction) {
            this.tableName = tableName;
            this.tableValueFunction = tableValueFunction;
        }

        public static <T> NameAndValue<T> of(String tableName, Function<T, Object> tableValueFunction) {
            return new NameAndValue<>(tableName, tableValueFunction);
        }

    }

    /**
     * 自定义 TableModel
     * 初始化表格信息
     */
    private class TablePanelModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return pageInfo != null
                    ? TablePanel.this.pageInfo.getRecords().size()
                    : 0;
        }

        @Override
        public int getColumnCount() {
            return TablePanel.this.tableHeaderDataList.size();
        }

        @Override
        public String getColumnName(int column) {
            return TablePanel.this
                    .tableHeaderDataList
                    .get(column)
                    .tableName;
        }


        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == tableHeaderDataList.size() - 1;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (pageInfo == null) {
                return null;
            }

            final T t = TablePanel.this.pageInfo
                    .getRecords()
                    .get(rowIndex);

            final Object apply = TablePanel.this
                    .tableHeaderDataList
                    .get(columnIndex)
                    .tableValueFunction
                    .apply(t);
            final TableColumn c = table.getColumnModel().getColumn(columnIndex);

            if (apply instanceof TableCellOperate) {
                if (pageInfo != null) {
                    //noinspection unchecked
                    ((TableCellOperate<T, ?>) apply).setData(pageInfo.getRecords());
                }

                c.setCellRenderer((TableCellRenderer) apply);
                c.setCellEditor((TableCellEditor) apply);
                c.setPreferredWidth(((TableCellOperate<?, ?>) apply).getWidth());
                return null;
            }

            return apply;

        }

    }

}

