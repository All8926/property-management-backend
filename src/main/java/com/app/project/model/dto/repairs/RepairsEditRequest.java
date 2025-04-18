package com.app.project.model.dto.repairs;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 编辑报修请求
 *
 * @author
 * @from
 */
@Data
public class RepairsEditRequest implements Serializable {

    /**
     * id
     */
    @NotNull
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
    private List<String> imageList;

    /**
     * 备注
     */
    private String remark;



    private static final long serialVersionUID = 1L;
}