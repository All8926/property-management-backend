package com.app.project.model.dto.visitor;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 更新访客请求
 *
 */
@Data
public class VisitorUpdateRequest implements Serializable {

    /**
     * id
     */
    @NotNull
    private Long id;


    /**
     * 0-待审核  1-已拒绝  2-已通过
     */
    @NotNull
    private Integer status;

    /**
     * 拒绝原因
     */
    private String reason;


    private static final long serialVersionUID = 1L;
}