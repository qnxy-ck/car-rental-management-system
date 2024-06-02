package com.qnxy.table.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 表格展示数据
 *
 * @author Qnxy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserInfo {

    private Integer id;
    private String username;
    private Integer age;
    private LocalDate birthday;

}
