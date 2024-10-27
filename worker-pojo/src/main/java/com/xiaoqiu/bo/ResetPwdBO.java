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
public class ResetPwdBO {

    private String adminId;
    private String password;
    private String rePassword;
}
