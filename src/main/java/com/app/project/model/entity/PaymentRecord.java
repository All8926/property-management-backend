package com.app.project.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 缴费项目
 * @TableName payment_record
 */
@TableName(value ="payment_record")
@Data
public class PaymentRecord implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 缴费项目id
     */
    private Long paymentId;

    /**
     * 缴费项目名称
     */
    private String paymentName;
    /**
     * 缴费金额
     */
    private String payAmount;

    /**
     * 缴费时间
     */
    private Date payDate;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 创建人姓名
     */
    private String userName;

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