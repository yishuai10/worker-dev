package com.xiaoqiu.qo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoqiu
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SmsContentQo {
    private String mobile;
    private String content;
}
