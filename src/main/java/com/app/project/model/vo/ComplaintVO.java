package com.app.project.model.vo;

import cn.hutool.json.JSONUtil;
import com.app.project.model.entity.Complaint;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 投诉视图
 *
 * @author
 * @from
 */
@Data
public class ComplaintVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 0-待处理  1-不予处理  2-已处理
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;


    /**
     * 图片列表
     */
    private List<String> imageList;

    /**
     * 创建用户信息
     */
    private UserVO user;


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
     * @param complaintVO
     * @return
     */
    public static Complaint voToObj(ComplaintVO complaintVO) {
        if (complaintVO == null) {
            return null;
        }
        Complaint complaint = new Complaint();
        BeanUtils.copyProperties(complaintVO, complaint);
        List<String> tagList = complaintVO.getImageList();
        complaint.setImage(JSONUtil.toJsonStr(tagList));
        return complaint;
    }

    /**
     * 对象转封装类
     *
     * @param complaint
     * @return
     */
    public static ComplaintVO objToVo(Complaint complaint) {
        if (complaint == null) {
            return null;
        }
        ComplaintVO complaintVO = new ComplaintVO();
        BeanUtils.copyProperties(complaint, complaintVO);
        complaintVO.setImageList(JSONUtil.toList(complaint.getImage(), String.class));
        return complaintVO;
    }
}
