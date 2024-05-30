package com.qnxy.window;

import com.qnxy.common.LoginType;
import com.qnxy.common.data.ui.RentalTableData;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 测试使用资源信息
 *
 * @author Qnxy
 */
public final class TestSource {


    private static final Map<LoginType, Map<String, String>> LOGIN_INFO_MAP = new HashMap<>();
    private static final SecureRandom rand = new SecureRandom();

    static {
        final Map<String, String> userLoginMap = new HashMap<>();
        userLoginMap.put("ck", "admin");
        userLoginMap.put("zhangSan", "admin123");

        LOGIN_INFO_MAP.put(LoginType.USER, userLoginMap);


        final Map<String, String> adminLoginMap = new HashMap<>();
        adminLoginMap.put("ck_admin", "admin");

        LOGIN_INFO_MAP.put(LoginType.ADMINISTRATOR, adminLoginMap);

    }


    /**
     * 用户/管理员登陆
     *
     * @param loginType 登陆类型 {@link LoginType}
     * @param username  用户名称
     * @param password  密码
     * @return 登陆成功返回true
     */
    public static boolean login(LoginType loginType, String username, String password) {

        final Map<String, String> loginMap;
        if ((loginMap = LOGIN_INFO_MAP.get(loginType)) == null) {
            return false;
        }

        return Optional.ofNullable(loginMap.get(username))
                .filter(it -> it.equals(password))
                .isPresent();
    }

    public static List<RentalTableData> getUserInfoList() {
        return Stream.generate(() -> new RentalTableData(
                        0,
                        "carModel " + rand.nextBoolean(),
                        "carOwner",
                        "price",
                        "carColor",
                        false,
                        "leasedUser",
                        "我是详情"
                ))
                .limit(16)
                .collect(Collectors.toList());

    }


}
