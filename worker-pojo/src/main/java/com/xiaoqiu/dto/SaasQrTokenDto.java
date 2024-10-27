package com.xiaoqiu.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author xiaoqiu
 */
@Data
@Builder
public class SaasQrTokenDto {
    private int status;
    private String preToken;
}
