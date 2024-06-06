package com.qnxy.common.data.entity;

import lombok.Data;

/**
 * 管理员实体类
 *
 * @author cyh
 * @since 2024/6/5
 */
@Data
public class Admin {

    /**
     * 编号
     */
    private Integer id;

    /**
     * 管理员名称
     */
    private String adminName;

    /**
     * 管理员密码
     */
    private String adminPassword;
}
