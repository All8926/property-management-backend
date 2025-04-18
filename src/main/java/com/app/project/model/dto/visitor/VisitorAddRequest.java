package com.app.project.model.dto.visitor;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 创建访客请求
 *
 * @author
 * @from
 */
@Data
public class VisitorAddRequest implements Serializable {

    /**
     * 来访人姓名
     */
    @NotBlank
    private String visitorName;

    /**
     * 来访时间
     */
    @NotNull
    private Date visitingTime;

    /**
     * 来访人手机号
     */
    private String visitorPhone;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;
}