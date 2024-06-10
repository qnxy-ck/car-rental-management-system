package com.qnxy.service;

import com.qnxy.common.LoginType;
import com.qnxy.common.data.entity.Admin;
import com.qnxy.common.data.entity.User;
import com.qnxy.jdbc.QuerySpace;
import com.qnxy.jdbc.UpdateSpace;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.function.Consumer;

/**
 * 登录页
 *
 * @author Qnxy
 */

@Slf4j
public class LoginPageService {


    // 用户登录
    public boolean login(LoginType loginType, String username, String password, Consumer<Integer> setUserIdConsumer) {

        if (loginType == LoginType.USER) {
            return new QuerySpace<>(User.class)
                    .sql("select * from user where user_name = ? and user_password = ?")
                    .params(username, password)
                    .optional()
                    .map(it -> {
                        setUserIdConsumer.accept(it.getId());
                        return it;
                    })
                    .isPresent();
        }

        return new QuerySpace<>(Admin.class)
                .sql("select * from admin where admin_name = ? and admin_password = ?")
                .params(username, password)
                .optional()
                .isPresent();

    }


    // true
    // false
    // 名称是否重复
    public RegisterStatus userRegister(String username, String password) {
        try {
            int update = new UpdateSpace()
                    .sql("insert into user(user_name, user_password) value(?,?)")
                    .params(username, password)
                    .update();

            return update > 0 ? RegisterStatus.SUC : RegisterStatus.ERR;
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                return RegisterStatus.DUPLICATE_NAME;
            }

            return RegisterStatus.ERR;
        }
    }

    public enum RegisterStatus {
        SUC,
        ERR,
        DUPLICATE_NAME
    }


}
