package com.app.project.model.dto.visitor;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 编辑访客请求
 *
 * @author
 * @from
 */
@Data
public class VisitorEditRequest  implements Serializable {

    /**
     * id
     */
    @NotNull
    private Long id;

    /**
     * 来访人姓名
     */
    private String visitorName;

    /**
     * 来访时间
     */
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