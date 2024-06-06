package com.qnxy.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/**
 * 登陆类型
 *
 * @author Qnxy
 */
@Getter
@RequiredArgsConstructor
public enum LoginType {

    USER("用户", 0),
    ADMINISTRATOR("管理员", 1),
    ;

    private final String typeText;
    private final int userType;

    public static Optional<LoginType> typeTextOf(String text) {
        return Arrays.stream(values())
                .filter(it -> it.getTypeText().equals(text))
                .findFirst();

    }
}
