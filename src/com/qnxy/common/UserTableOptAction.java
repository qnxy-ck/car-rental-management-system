package com.qnxy.common;

import com.qnxy.window.TableCellOperate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author cyh
 * @since 2024/6/7
 */
@RequiredArgsConstructor
@Getter
public enum UserTableOptAction implements TableCellOperate.ActionName {
    RENT("租用"),
    DETAILS("详情");

    private final String actionName;
}