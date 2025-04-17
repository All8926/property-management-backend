package com.app.project.model.dto.complaint;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 编辑投诉请求
 *
 * @author
 * @from
 */
@Data
public class ComplaintEditRequest implements Serializable {

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

    private static final long serialVersionUID = 1L;
}