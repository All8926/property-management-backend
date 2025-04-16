package com.app.project.model.vo;

import cn.hutool.json.JSONUtil;
import com.app.project.model.entity.PaymentRecord;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 缴费记录视图
 *
 * @author
 * @from
 */
@Data
public class PaymentRecordVO implements Serializable {

    /**
     * id
     */
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
     * 创建人名称
     */
    private String userName;

    /**
     * 创建时间
     */
    private Date createTime;
}
