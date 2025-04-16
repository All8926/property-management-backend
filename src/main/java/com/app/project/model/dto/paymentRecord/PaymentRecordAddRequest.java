package com.app.project.model.dto.paymentRecord;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建缴费记录请求
 *
 * @author
 * @from
 */
@Data
public class PaymentRecordAddRequest implements Serializable {


    /**
     * 缴费项目id
     */
    @NotNull(message = "缴费项目id不能为空")
    private Long paymentId;

}