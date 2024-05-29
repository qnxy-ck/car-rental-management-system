package com.qnxy.window;

import com.qnxy.common.data.PageInfo;
import com.qnxy.window.admin.AdminOptTableCellEditor;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 自定义表格
 *
 * @author Qnxy
 */
public class TablePanel<T> extends JPanel {

    /**
     * 表头信息
     */
    private final List<NameAndValue<T>> tableHeaderDataList;
    private final JTable table = new JTable();
    private final Map<BottomLabelE, JLabel> bottomLabelJLabelMap = new LinkedHashMap<>();
    private final Supplier<PageInfo<T>> initData;
    private final Supplier<PageInfo<T>> upPage;
    private final Supplier<PageInfo<T>> nextPage;
    /**
     * 表格数据
     */
    private PageInfo<T> pageInfo = null;

    /**
     * 创建一个自定义表格
     *
     * @param tableHeaderDataList 当前表格表头信息和初始化数据方式
     */
    public TablePanel(
            List<NameAndValue<T>> tableHeaderDataList,
            Supplier<PageInfo<T>> initData,
            Supplier<PageInfo<T>> upPage,
            Supplier<PageInfo<T>> nextPage
    ) {
        this.tableHeaderDataList = tableHeaderDataList;
        this.initData = initData;
        this.upPage = upPage;
        this.nextPage = nextPage;

        // 设置自定义 tableModel
        table.setModel(new TablePanelModel());
        // 设置表格为单行选中
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // 设置表格行高
        table.setRowHeight(30);

        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, r);

        // 将表格设置到当前 JScrollPane 中
        final JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1000, 500));

        setLayout(new BorderLayout());

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPagePanel(new FlowLayout(FlowLayout.RIGHT, 15, 20)), BorderLayout.SOUTH);

    }

    /**
     * 底部信息按钮
     */
    private JPanel bottomPagePanel(FlowLayout flowLayout) {
        return new JPanel() {{
            setLayout(flowLayout);
            setPreferredSize(new Dimension(0, 60));

            add(new JButton("上一页") {{
                addActionListener(e -> {
                    final PageInfo<T> tPageInfo = TablePanel.this.upPage.get();
                    TablePanel.this.refreshTableData(tPageInfo);
                });
            }});

            add(new JButton("下一页") {{
                addActionListener(e -> {
                    final PageInfo<T> tPageInfo = TablePanel.this.nextPage.get();
                    TablePanel.this.refreshTableData(tPageInfo);
                });
            }});

//            add(new JLabel("跳转到:"));
//            add(jumpTextField());


            for (BottomLabelE value : BottomLabelE.values()) {
                final JLabel jLabel = new JLabel(value.labelText + value.defaultNum);
                bottomLabelJLabelMap.put(value, jLabel);
                add(jLabel);
            }

        }};

    }

    private void updateBottomLabelText(BottomLabelE bottomLabelE, int num) {
        Optional.ofNullable(this.bottomLabelJLabelMap.get(bottomLabelE))
                .ifPresent(it -> it.setText(bottomLabelE.labelText + num));
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

    /**
     * 刷新当前表格数据信息
     *
     * @param pageInfo 当前表格数据集合
     */
    public void refreshTableData(PageInfo<T> pageInfo) {
        this.pageInfo = pageInfo;

        Optional.ofNullable(pageInfo)
                .ifPresent(it -> {
                    updateBottomLabelText(BottomLabelE.CURRENT_PAGE, it.getCurrentPage());
                    updateBottomLabelText(BottomLabelE.PAGE_SIZE, it.getPageSize());
                    updateBottomLabelText(BottomLabelE.TOTAL, it.getTotal());
                });
        table.updateUI();

    }

    private enum BottomLabelE {
        CURRENT_PAGE("当前页: ", 1),
        PAGE_SIZE("当前页数量: ", 0),
        TOTAL("总页数: ", 0);

        private final String labelText;
        private final int defaultNum;

        BottomLabelE(String labelText, int defaultNum) {
            this.labelText = labelText;
            this.defaultNum = defaultNum;
        }
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

            if (apply instanceof AdminOptTableCellEditor) {
                if (pageInfo != null) {
                    //noinspection unchecked
                    ((AdminOptTableCellEditor<T>) apply).setData(pageInfo.getRecords());
                }

                c.setCellRenderer((TableCellRenderer) apply);
                c.setCellEditor((TableCellEditor) apply);
                c.setPreferredWidth(110);
                return null;
            }

            return apply;

        }

    }

}




