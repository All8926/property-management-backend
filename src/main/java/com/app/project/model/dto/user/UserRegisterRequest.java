package com.app.project.model.dto.user;

import java.io.Serializable;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户注册请求体
 *
 * @author 
 * @from 
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    @NotBlank(message = "账号不能为空")
    @Size(max = 10, message = "不能超过10个字符")
    private String userAccount;

    @NotBlank(message = "密码不能为空")
    private String userPassword;

    @NotBlank(message = "确认密码不能为空")
    private String checkPassword;

    @Size(max = 10, message = "不能超过10个字符")
    private String userName;

    @Size(max = 11, message = "不能超过11个位")
    private String userPhone;
}
