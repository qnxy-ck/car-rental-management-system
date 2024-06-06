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

    private int uid;
    private String userName;
    private String userPassword;

}
