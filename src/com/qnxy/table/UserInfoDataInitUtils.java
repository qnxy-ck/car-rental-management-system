package com.qnxy.table;

import com.qnxy.table.data.ProductInfo;
import com.qnxy.table.data.UserInfo;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Qnxy
 */
public final class UserInfoDataInitUtils {

    private static int accessCount = 0;
    private static final SecureRandom random = new SecureRandom();
    private static int productInfoCount = 0;

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
                                .setId(accessCount += 1)
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

    public static List<ProductInfo> productInfoList() {
        return Stream.generate(
                        () -> new ProductInfo()
                                .setId(productInfoCount += 1)
                                .setProductName("cyh66")
                                .setProductCode("9999")
                                .setProductType("高手")
                                .setProductPrice("99999")
                                .setSoldOut(random.nextBoolean())
                )
                .limit(20)
                .toList();
    }


}
