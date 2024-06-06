package com.qnxy.table.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author cyh
 * @since 2024/6/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// 开启链式编程
@Accessors(chain = true)
public class EmployeesInfo {

    private Integer id;
    private String name;
    private Integer age;
    private double height;


}
