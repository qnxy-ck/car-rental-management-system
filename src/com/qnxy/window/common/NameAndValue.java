package com.qnxy.window.common;

/**
 * @author cyh
 * @since 2024/6/10
 */

import com.qnxy.window.TableCellOperate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 表格表头数据和对应数据初始化方式
 * <p>
 * 如果表头中需要添加自定义按钮, 则 Function<T, Object> tableValueFunction 中 Object 类应实现 {@link TableCellOperate} 类
 *
 * @param <T>
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NameAndValue<T> {
    private final String tableTitle;
    private final Function<T, Object> valueGetFun;
    private final Supplier<TableCellOperate<T, ?>> cellOperateSupplier;

    public static <T> NameAndValue<T> of(String tableTitle, Function<T, Object> valueGetFun) {
        return new NameAndValue<>(tableTitle, valueGetFun, null);
    }

    public static <T> NameAndValue<T> of(String tableTitle, Supplier<TableCellOperate<T, ?>> cellOperateSupplier) {
        return new NameAndValue<>(tableTitle, null, cellOperateSupplier);
    }

}