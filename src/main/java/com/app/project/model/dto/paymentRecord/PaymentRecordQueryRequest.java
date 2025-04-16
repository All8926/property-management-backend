package com.app.project.model.dto.paymentRecord;

import com.app.project.common.PageRequest;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询缴费记录请求
 *
 * @author
 * @from
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PaymentRecordQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 缴费项目名称
     */
    private String paymentName;


    /**
     * 缴费时间
     */
    private Date payDate;

    /**
     * 缴费人姓名
     */
    private String userName;

    private static final long serialVersionUID = 1L;
}