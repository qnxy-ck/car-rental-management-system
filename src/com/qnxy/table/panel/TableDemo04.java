package com.qnxy.table.panel;

import com.qnxy.table.UserInfoDataInitUtils;
import com.qnxy.table.data.ProductInfo;

import javax.swing.*;
import java.util.List;

/**
 * @author Qnxy
 */
public class TableDemo04 extends JPanel {


    private final TablePanel2<ProductInfo> tablePanel2;

    public TableDemo04() {

        List<TablePanel2.TitleAndValue<ProductInfo>> titleAndValueList = List.of(
                new TablePanel2.TitleAndValue<>("id", ProductInfo::getId),
                new TablePanel2.TitleAndValue<>("productName", ProductInfo::getProductName),
                new TablePanel2.TitleAndValue<>("productCode", ProductInfo::getProductCode),
                new TablePanel2.TitleAndValue<>("productType", ProductInfo::getProductType),
                new TablePanel2.TitleAndValue<>("productPrice", ProductInfo::getProductPrice),
                new TablePanel2.TitleAndValue<>("是否已售空", it -> it.getSoldOut() ? "已售空" : "哈哈哈哈")
        );

        this.tablePanel2 = new TablePanel2<>(
                titleAndValueList,
                UserInfoDataInitUtils::productInfoList
        );

        add(this.tablePanel2);


    }
}
