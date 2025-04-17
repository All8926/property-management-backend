package com.app.project.model.dto.complaint;

import com.app.project.common.PageRequest;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询投诉请求
 *
 * @author
 * @from
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ComplaintQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;


    /**
     * 0-待处理  1-不予处理  2-已处理
     */
    private Integer status;




    private static final long serialVersionUID = 1L;
}