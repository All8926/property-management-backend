package com.app.project.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 访客登记
 * @TableName visitor
 */
@TableName(value ="visitor")
@Data
public class Visitor implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
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
     * 0-待审核  1-已拒绝  2-已通过
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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}