package com.qnxy.window.common;

import com.qnxy.common.data.PageInfo;
import com.qnxy.window.QuickListenerAdder;
import com.qnxy.window.TableCellOperate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

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
        put(new JLabel("总条数: 0"), (label, pageInfo) -> label.setText("总条数: " + pageInfo.getTotal()));
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
    public TablePanel(
            List<NameAndValue<T>> tableHeaderDataList,
            BiFunction<Integer, DataInitType, PageInfo<T>> dataGetFunc
    ) {
        this.tableHeaderDataList = tableHeaderDataList;
        this.dataGetFunc = dataGetFunc;

        initTablePanel();
        initCellOperate();

        invokeDataGetFun(DataInitType.INIT);
    }

    /**
     * 初始化表格自定义操作
     */
    private void initCellOperate() {
        final TableColumnModel columnModel = this.table.getColumnModel();

        IntStream.range(0, this.tableHeaderDataList.size()).forEach(i -> {
            final Supplier<? extends TableCellOperate<T, ?>> cellOperateSupplier = tableHeaderDataList.get(i).cellOperateSupplier;
            if (cellOperateSupplier != null) {
                final TableColumn column = columnModel.getColumn(i);
                final TableCellOperate<T, ?> tableCellOperate = cellOperateSupplier.get();

                column.setPreferredWidth(tableCellOperate.getWidth());

                column.setCellRenderer(tableCellOperate);
                column.setCellEditor(tableCellOperate);
            }
        });
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
        scrollPane.setPreferredSize(new Dimension(1200, 506));

        setLayout(new BorderLayout());

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPagePanel(), BorderLayout.SOUTH);
    }

    /**
     * 底部信息按钮
     */
    private JPanel bottomPagePanel() {
        return new JPanel() {{
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(10, 50, 6, 20));

            final JPanel leftPanel = new JPanel();
            final JPanel rightPanel = new JPanel();

            final JLabel label = new JLabel("By: ck - qnxy1997@gmail.com", SwingConstants.CENTER);
            label.setFont(new Font("宋体", Font.ITALIC, 17));
            label.setPreferredSize(new Dimension(260, 30));
            label.setForeground(new Color(110, 83, 80, 255));
            leftPanel.add(label);


            new QuickListenerAdder(rightPanel)
                    .add(new JButton("上一页"), e -> invokeDataGetFun(DataInitType.UP_PAGE))
                    .add(new JButton("下一页"), e -> invokeDataGetFun(DataInitType.NEXT_PAGE));
            bottomLabelJLabelMap.keySet().forEach(rightPanel::add);


            add(leftPanel, BorderLayout.WEST);
            add(rightPanel, BorderLayout.EAST);
        }};
    }


    private void invokeDataGetFun(DataInitType dataInitType) {
        final Integer currentPage = Optional.ofNullable(this.pageInfo)
                .map(PageInfo::getCurrentPage)
                .orElse(1);

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
        this.pageInfo = pageInfo;
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
     * <p>
     * 如果表头中需要添加自定义按钮, 则 Function<T, Object> tableValueFunction 中 Object 类应实现 {@link TableCellOperate} 类
     *
     * @param <T>
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NameAndValue<T> {
        private final String tableTitle;
        private final Function<T, Object> valueGetFun;
        private final Supplier<TableCellOperate<T, ?>> cellOperateSupplier;

        public static <T> NameAndValue<T> of(String tableTitle, Function<T, Object> valueGetFun) {
            return new NameAndValue<>(tableTitle, valueGetFun, null);
        }

        public static <T> NameAndValue<T> of(String tableTitle, Supplier<TableCellOperate<T, ?>> cellOperateSupplier) {
            return new NameAndValue<>(tableTitle, null, cellOperateSupplier);
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
                    .tableTitle;
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

            final T t = TablePanel.this.pageInfo.getRecords().get(rowIndex);

            return Optional.ofNullable(TablePanel.this.tableHeaderDataList.get(columnIndex).valueGetFun)
                    .map(it -> it.apply(t))
                    .orElse(t);
        }

    }

}

