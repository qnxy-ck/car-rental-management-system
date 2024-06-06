package com.qnxy.common.data.entity;

import lombok.Data;

/**
 * 用户实体类
 *
 * @author cyh
 * @since 2024/6/4
 */
@Data
public class User {


    /**
     * 编号
     */
    private Integer id;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPassword;

}
