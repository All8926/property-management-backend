package com.app.project.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 访客视图
 *
 * @author
 * @from
 */
@Data
public class VisitorVO implements Serializable {

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
     * 备注
     */
    private String remark;

    /**
     * 0-待审核  1-已通过  2-已拒绝
     */
    private Integer status;

    /**
     * 拒绝原因
     */
    private String reason;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 创建用户信息
     */
    private UserVO user;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
