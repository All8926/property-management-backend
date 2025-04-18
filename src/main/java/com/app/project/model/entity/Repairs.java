package com.app.project.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 报修记录
 * @TableName repairs
 */
@TableName(value ="repairs")
@Data
public class Repairs implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 详情
     */
    private String content;

    /**
     * 图片
     */
    private String image;

    /**
     * 备注
     */
    private String remark;

    /**
     * 0-审核中  1-已拒绝  2-维修中  3-无法维修  4-待评价  5-已完成
     */
    private Integer status;

    /**
     * 报修人
     */
    private Long userId;

    /**
     * 维修人
     */
    private Long servicemanId;

    /**
     * 评价
     */
    private String comment;

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