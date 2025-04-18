package com.app.project.model.dto.repairs;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class RepairsCommentRequest implements Serializable {
    /**
     * id
     */
    @NotNull
    private Long id;

    /**
     * 评价内容
     */
    @NotBlank(message = "评价内容不能为空")
    private String comment;


    private static final long serialVersionUID = 1L;
}
