package com.app.project.model.vo;

import cn.hutool.json.JSONUtil;
import com.app.project.model.entity.PaymentItem;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 缴费项目视图
 *
 * @author
 * @from
 */
@Data
public class PaymentItemVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 金额
     */
    private String amount;

    /**
     * 过期时间
     */
    private Date expirationTime;

    /**
     * 简介
     */
    private String profile;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 创建人信息
     */
    private UserVO createUser;

}
