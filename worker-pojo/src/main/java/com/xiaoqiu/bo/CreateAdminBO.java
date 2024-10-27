package com.xiaoqiu.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author xiaoqiu
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateAdminBO {

    private String username;
    private String password;
    private String remark;

}
