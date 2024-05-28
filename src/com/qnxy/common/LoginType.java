package com.qnxy.common;

import java.util.Arrays;
import java.util.Optional;

/**
 * 登陆类型
 *
 * @author Qnxy
 */
public enum LoginType {

    USER("用户"),
    ADMINISTRATOR("管理员"),
    ;

    private final String typeText;

    LoginType(String typeText) {
        this.typeText = typeText;
    }

    public String getTypeText() {
        return typeText;
    }

    public int getTypeNum() {
        return this.ordinal();
    }


    public static Optional<LoginType> typeTextOf(String text) {
        return Arrays.stream(values())
                .filter(it -> it.getTypeText().equals(text))
                .findFirst();
        
    }
}
