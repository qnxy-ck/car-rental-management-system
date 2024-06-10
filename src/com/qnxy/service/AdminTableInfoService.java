package com.qnxy.service;

import com.qnxy.common.data.PageInfo;
import com.qnxy.common.data.entity.CarInformation;
import com.qnxy.jdbc.QuerySpace;
import com.qnxy.jdbc.UpdateSpace;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cyh
 * @since 2024/6/7
 */
@Slf4j
public class AdminTableInfoService {

    private static final int PAGE_SIZE = 16;

    private static int getCount(Integer id) {
        QuerySpace<Void> sqlPageQuery = new QuerySpace<>(Void.class);

        final StringBuilder sqlPage = new StringBuilder("select count(*) from car_information");
        if (id != null) {
            sqlPage.append(" where id = ?");
            sqlPageQuery.param(id);
        }


        return sqlPageQuery.sql(sqlPage.toString()).count();
    }

    /**
     * 管理员车辆信息录入
     *
     * @param carInformation 添加信息
     * @return 是否成功
     */
    public boolean addAdminTableInfo(CarInformation carInformation) {
        try {
            return new UpdateSpace()
                    .sql("insert into car_information(car_type, car_renters, price, color, information) value (?, ?, ?, ?, ?)")
                    .params(
                            carInformation.getCarType(),
                            carInformation.getCarRenters(),
                            carInformation.getPrice(),
                            carInformation.getColor(),
                            carInformation.getInformation()
                    )
                    .update() > 0;
        } catch (SQLException e) {
            log.error("管理员车辆信息录入", e);
            return false;
        }
    }

    /**
     * 查询管理员表格数据
     *
     * @param id          指定id查询, 可为空
     * @param currentPage 查询那一页
     * @return .
     */
    public PageInfo<CarInformation> selectTableInfo(Integer id, Integer currentPage) {
        int count = getCount(id);

        QuerySpace<CarInformation> sqlQuery = new QuerySpace<>(CarInformation.class);

        final StringBuilder sql = new StringBuilder("select * from car_information");
        if (id != null) {
            sql.append(" where id = ?");
            sqlQuery.param(id);
        }

        sql.append(" order by created_at desc limit ?, ").append(PAGE_SIZE);

        List<CarInformation> list = sqlQuery.sql(sql.toString())
                .param((currentPage - 1) * PAGE_SIZE)
                .list();

        return new PageInfo<>(list, currentPage, count);
    }


    public boolean deleteById(Integer id) {
        try {
            return new UpdateSpace()
                    .sql("delete from car_information where id = ?")
                    .params(id)
                    .update() > 0;
        } catch (SQLException e) {
            return false;
        }
    }


    public boolean updateById(CarInformation data) {
        final StringBuilder updateSql = new StringBuilder();
        List<String> sqlPart = new ArrayList<>();
        final UpdateSpace updateSpace = new UpdateSpace();

        updateSql.append("update car_information");

        if (data.getCarType() != null && !data.getCarType().isEmpty()) {
            sqlPart.add(" car_type = ?");
            updateSpace.param(data.getCarType());
        }

        if (data.getCarRenters() != null && !data.getCarRenters().isEmpty()) {
            sqlPart.add(" car_renters = ?");
            updateSpace.param(data.getCarRenters());
        }

        if (data.getPrice() != null && !data.getPrice().isEmpty()) {
            sqlPart.add(" price = ?");
            updateSpace.param(data.getPrice());
        }

        if (data.getColor() != null && !data.getColor().isEmpty()) {
            sqlPart.add(" color = ?");
            updateSpace.param(data.getColor());
        }

        if (data.getInformation() != null && !data.getInformation().isEmpty()) {
            sqlPart.add(" information = ?");
            updateSpace.param(data.getInformation());
        }


        if (sqlPart.isEmpty()) {
            return true;
        }

        updateSql.append(" set")
                .append(
                        String.join(" , ", sqlPart)
                )
                .append(" where id = ?");

        updateSpace.param(data.getId());

        try {
            return updateSpace.sql(updateSql.toString())
                    .update() > 0;
        } catch (SQLException e) {
            return false;
        }

    }
}
