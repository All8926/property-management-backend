package com.app.project.model.dto.complaint;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 更新投诉请求
 *
 */
@Data
public class ComplaintUpdateRequest implements Serializable {

    /**
     * id
     */
    @NotNull
    private Long id;


    /**
     * 备注
     */
    private String remark;

    /**
     * 0-待处理  1-不予处理  2-已处理
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}