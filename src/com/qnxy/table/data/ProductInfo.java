package com.qnxy.table.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Qnxy
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfo {

    private int id;
    private String productName;
    private String productCode;
    private String productType;
    private String productPrice;
    private Boolean soldOut;
}
