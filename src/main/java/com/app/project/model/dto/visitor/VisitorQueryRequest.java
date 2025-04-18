package com.app.project.model.dto.visitor;

import com.app.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询访客请求
 *
 * @author
 * @from
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VisitorQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
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
     * 0-待审核  1-已拒绝  2-已通过
     */
    private Integer status;


    private static final long serialVersionUID = 1L;
}