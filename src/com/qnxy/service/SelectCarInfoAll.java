package com.qnxy.service;

import com.qnxy.common.LoginType;
import com.qnxy.common.data.entity.CarInformation;
import com.qnxy.jdbc.QuerySpace;

import java.util.List;

/**
 * 查询车辆信息功能
 *
 * @author cyh
 * @since 2024/6/5
 */
public class SelectCarInfoAll {

    public static void main(String[] args) {


        List<CarInformation> list = new QuerySpace<>(CarInformation.class)
                .sql("select * from car_information")
                .list();


        for (CarInformation carInformation : list) {
            System.out.println(carInformation);
        }

        // ??

    }

    public List<CarInformation> findCarInfos(LoginType loginType) {
        if (loginType == LoginType.USER) {
            return new QuerySpace<>(CarInformation.class)
                    .sql("select * from car_information")
                    .list();
        }
        return new QuerySpace<>(CarInformation.class)
                .sql("select id, cartype, carower, price, color, hire from car_information from car_information")
                .list();
    }

}
