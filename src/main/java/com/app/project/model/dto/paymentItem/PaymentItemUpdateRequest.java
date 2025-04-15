package com.app.project.model.dto.paymentItem;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 更新缴费项目请求
 *
 */
@Data
public class PaymentItemUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    @Size(max = 20, message = "不能超过20个字符")
    private String name;

    /**
     * 过期时间
     */
    private Date expirationTime;

    /**
     * 简介
     */
    @Size(max = 500, message = "不能超过500个字符")
    private String profile;


    private static final long serialVersionUID = 1L;
}