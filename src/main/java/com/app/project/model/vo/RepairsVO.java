package com.app.project.model.vo;

import cn.hutool.json.JSONUtil;
import com.app.project.model.entity.Complaint;
import com.app.project.model.entity.Repairs;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 报修视图
 *
 * @author
 * @from
 */
@Data
public class RepairsVO implements Serializable {

    /**
     * id
     */
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

    /**
     * 0-审核中  1-已拒绝  2-维修中  3-无法维修  4-待评价  5-已完成
     */
    private Integer status;

    /**
     * 报修人
     */
    private Long userId;

    /**
     * 报修用户信息
     */
    private UserVO user;

    /**
     * 维修人
     */
    private Long servicemanId;

    /**
     * 维修用户信息
     */
    private UserVO servicemanUser;

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
     * 封装类转对象
     *
     * @param repairsVO
     * @return
     */
    public static Repairs voToObj(RepairsVO repairsVO) {
        if (repairsVO == null) {
            return null;
        }
        Repairs repairs = new Repairs();
        BeanUtils.copyProperties(repairsVO, repairs);
        List<String> tagList = repairsVO.getImageList();
        repairs.setImage(JSONUtil.toJsonStr(tagList));
        return repairs;
    }

    /**
     * 对象转封装类
     *
     * @param repairs
     * @return
     */
    public static RepairsVO objToVo(Repairs repairs) {
        if (repairs == null) {
            return null;
        }
        RepairsVO repairsVO = new RepairsVO();
        BeanUtils.copyProperties(repairs, repairsVO);
        repairsVO.setImageList(JSONUtil.toList(repairs.getImage(), String.class));
        return repairsVO;
    }
}
