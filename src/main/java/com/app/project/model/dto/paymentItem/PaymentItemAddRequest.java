package com.app.project.model.dto.paymentItem;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建缴费项目请求
 *
 * @author
 * @from
 */
@Data
public class PaymentItemAddRequest implements Serializable {


    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    @Size(max = 20, message = "不能超过20个字符")
    private String name;

    /**
     * 金额
     */
    @NotBlank(message = "金额不能为空")
    @Size(max = 10, message = "不能超过10个字符")
    private String amount;

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