package com.qnxy.service;

import com.qnxy.common.data.PageInfo;
import com.qnxy.common.data.entity.CarInformation;
import com.qnxy.jdbc.QuerySpace;
import com.qnxy.jdbc.UpdateSpace;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户表格数据操作
 *
 * @author cyh
 * @since 2024/6/7
 */
public class UserTableInfoService {

    private static final int PAGE_SIZE = 16;


    private static <T> QuerySpace<T> getSql(Class<T> clazz, boolean isCount, Integer id, boolean allOrRent, Integer userId, int currentPage) {
        final StringBuilder sqlCount = new StringBuilder();
        QuerySpace<T> countQuerySpace = new QuerySpace<>(clazz);

        List<String> sqlPart = new ArrayList<>();

        if (isCount) {
            sqlCount.append("select count(*) from car_information");
        } else {
            sqlCount.append("select * from car_information");
        }

        if (!allOrRent) {
            sqlPart.add(" uid is null");
        }


        if (id != null) {
            sqlPart.add(" id = ?");
            countQuerySpace.param(id);
        }

        if (userId != null) {
            sqlPart.add(" uid = ?");
            countQuerySpace.param(userId);
        }


        if (!sqlPart.isEmpty()) {
            sqlCount.append(" where ");

            sqlCount.append(
                    String.join(" and ", sqlPart)
            );

        }

        if (!isCount) {
            sqlCount.append(" order by created_at desc");
            sqlCount.append(" limit ?, ")
                    .append(UserTableInfoService.PAGE_SIZE);
            countQuerySpace.param((currentPage - 1) * PAGE_SIZE);
        }

        return countQuerySpace.sql(sqlCount.toString());
    }


    /**
     * 用户表格数据查询
     *
     * @param id        用户查询时使用, 可为空. 为空时不参与查询
     * @param allOrRent 查询全部还是可租用
     * @param userId    当前登录用户的id, 查询自己已租用的车, 如果为空不参与查询
     * @return .
     */
    public PageInfo<CarInformation> selectUserCarInfo(Integer id, Boolean allOrRent, Integer userId, Integer currentPage) {
        int count = getSql(Void.class, true, id, allOrRent, userId, currentPage)
                .count();

        List<CarInformation> informationList = getSql(CarInformation.class, false, id, allOrRent, userId, currentPage)
                .list();

        return new PageInfo<>(informationList, currentPage, count);
    }

    /**
     * @param id
     * @param uid
     * @return
     */
    public RentUpdate rent(Integer id, Integer uid) {
        boolean present = new QuerySpace<>(CarInformation.class)
                .sql("select * from car_information where id = ? and uid is not null")
                .param(id)
                .optional()
                .isPresent();

        if (present) {
            return RentUpdate.EXISTING;
        }

        try {
            boolean b = new UpdateSpace()
                    .sql("update car_information set uid = ? where id = ?")
                    .params(uid, id)
                    .update() > 0;
            return b ? RentUpdate.SUC : RentUpdate.ERR;
        } catch (SQLException e) {
            return RentUpdate.ERR;
        }
    }

    public enum RentUpdate {
        SUC,
        ERR,
        EXISTING
    }

}
