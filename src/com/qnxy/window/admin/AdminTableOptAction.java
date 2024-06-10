package com.qnxy.window.admin;

import com.qnxy.window.ActionName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author cyh
 * @since 2024/6/10
 */
@RequiredArgsConstructor
@Getter
public enum AdminTableOptAction implements ActionName {
    UPDATE("更新"),
    DELETE("删除"),
    DETAILS("详情");

    private final String actionName;
}
