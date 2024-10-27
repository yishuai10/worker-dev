package com.xiaoqiu.bo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * @author xiaoqiu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.class)
public class LoginSmsBo {

    @NotBlank(message = "手机号不能为空")
    @Length(max = 11, min = 11, message = "手机号长度不正确")
    private String mobile;
    @NotBlank
    private String smsCode;
}
