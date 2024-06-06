package com.qnxy.common.data.entity;

import lombok.Data;

/**
 * 车辆信息实体类
 *
 * @author cyh
 * @since 2024/6/5
 */
@Data
public class CarInformation {

    private Integer id;
    private String cartype;
    private String carower;
    private String price;
    private String color;
    private String hire;
    private String information;
    private String username;

}
