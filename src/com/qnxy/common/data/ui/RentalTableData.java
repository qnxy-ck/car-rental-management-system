package com.qnxy.common.data.ui;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Qnxy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalTableData {

    /**
     * 编号
     */
    private Integer id;

    /**
     * 车型号
     */
    private String carModel;

    /**
     * 所有者
     */
    private String carOwner;

    /**
     * 价格(元/天)
     */
    private String price;

    /**
     * 车颜色
     */
    private String carColor;

    /**
     * 是否被租用
     */
    private Boolean leased;

    /**
     * 租用者
     */
    private String leasedUser;

    /**
     * 详情信息
     */
    private String details;


}
