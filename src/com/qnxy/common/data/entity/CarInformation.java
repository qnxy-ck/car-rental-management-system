package com.qnxy.common.data.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 车辆信息实体类
 *
 * @author cyh
 * @since 2024/6/6
 */
@Data
public class CarInformation {

    /**
     * 编号
     */
    private Integer id;

    /**
     * 类型
     */
    private String carType;

    /**
     * 车主
     */
    private String carRenters;

    /**
     * 价格(元/天)
     */
    private String price;

    /**
     * 颜色
     */
    private String color;

    /**
     * 车辆信息信息
     */
    private String information;

    /**
     * 租用人
     */
    private Integer uid;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
