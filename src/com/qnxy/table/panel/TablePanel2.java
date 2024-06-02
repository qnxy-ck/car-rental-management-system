package com.qnxy.table.panel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Qnxy
 */
public class TablePanel2<DATA> extends JPanel {

    private final JTable table = new JTable();

    private final List<DATA> rowDataList = new ArrayList<>();
    private final List<TitleAndValue<DATA>> titleAndValueList;
    private final Supplier<List<DATA>> dataListFun;


    public TablePanel2(
            List<TitleAndValue<DATA>> titleAndValueList,
            Supplier<List<DATA>> dataListFun
    ) {
        this.titleAndValueList = titleAndValueList;
        this.dataListFun = dataListFun;


        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1200, 700));


        table.setRowHeight(35);
        this.table.setModel(new TablePanelTableModel());

        // JScrollPane 给表格增加滚动条, 并且使用 JScrollPane 会自动展示表头信息
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        initTableData();
    }


    private void initTableData() {
        final List<DATA> dataList = dataListFun.get();

        this.rowDataList.clear();
        this.rowDataList.addAll(dataList);

        // 使表格重新绘制, 根据最新的素具
        this.table.updateUI();
    }

    /**
     * 表头和数据获取的映射关系
     */
    public record TitleAndValue<T>(
            String title,
            Function<T, Object> valueGetFun
    ) {

    }

    private class TablePanelTableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return rowDataList.size();
        }

        @Override
        public int getColumnCount() {
            return titleAndValueList.size();
        }

        @Override
        public String getColumnName(int column) {
            return titleAndValueList.get(column).title();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            final DATA data = rowDataList.get(rowIndex);

            TitleAndValue<DATA> dataTitleAndValue = titleAndValueList.get(columnIndex);
            return dataTitleAndValue.valueGetFun()
                    .apply(data);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }
    }


}
