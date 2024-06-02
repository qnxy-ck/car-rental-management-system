package com.qnxy.table;

import com.qnxy.table.data.UserInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * @author Qnxy
 */
public final class UserInfoDataInitUtils {
    private static final AtomicInteger accessCount = new AtomicInteger(0);

    /**
     * jdk >= 16 才可以正常使用
     * <p>
     * 生成表格数据 {@link UserInfo }
     *
     * @param listSize 要求返回集合数量
     */
    public static List<UserInfo> userInfoList(int listSize) {
        return Stream.generate(
                        () -> new UserInfo()
                                .setId(accessCount.getAndIncrement())
                                .setAge(10)
                                .setUsername("ck")
                                .setBirthday(LocalDate.now())
                )
                .limit(listSize)
                .toList();

    }

    public static List<UserInfo> userInfoList() {
        return userInfoList(20);
    }


}
