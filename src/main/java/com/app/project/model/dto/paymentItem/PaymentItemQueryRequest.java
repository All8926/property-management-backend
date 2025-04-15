package com.app.project.model.dto.paymentItem;

import com.app.project.common.PageRequest;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询缴费项目请求
 *
 * @author
 * @from
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentItemQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 金额
     */
    private String amount;

    /**
     * 过期时间
     */
    private Date expirationTime;

    /**
     * 简介
     */
    private String profile;

    /**
     * 创建人
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}