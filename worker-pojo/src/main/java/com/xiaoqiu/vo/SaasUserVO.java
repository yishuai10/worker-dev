package com.xiaoqiu.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author xiaoqiu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SaasUserVO {

    private String nickname;
    private String realName;
    private String face;

}