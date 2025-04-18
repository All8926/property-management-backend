package com.app.project.model.dto.repairs;

import com.app.project.common.PageRequest;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询报修请求
 *
 * @author
 * @from
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RepairsQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;


    /**
     * 0-审核中  1-已拒绝  2-维修中  3-无法维修  4-待评价  5-已完成
     */
    private Integer status;

    /**
     * 维修人
     */
    private Long servicemanId;



    private static final long serialVersionUID = 1L;
}