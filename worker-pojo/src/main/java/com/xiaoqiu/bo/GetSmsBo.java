package com.xiaoqiu.bo;

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
public class GetSmsBo {

    @NotBlank(message = "手机号不能为空")
    @Length(max = 11, min = 11, message = "手机号长度不正确")
    private String mobile;
}
