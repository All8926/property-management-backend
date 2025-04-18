package com.app.project.service;


import com.app.project.model.dto.complaint.ComplaintQueryRequest;
import com.app.project.model.dto.paymentItem.PaymentItemQueryRequest;
import com.app.project.model.entity.Complaint;
import com.app.project.model.entity.PaymentItem;
import com.app.project.model.entity.User;
import com.app.project.model.vo.ComplaintVO;
import com.app.project.model.vo.PaymentItemVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author Administrator
* @description 针对表【complaint(投诉记录)】的数据库操作Service
* @createDate 2025-04-16 21:44:19
*/
public interface ComplaintService extends IService<Complaint> {
    /**
     * 获取查询条件
     *
     * @param complaintQueryRequest
     * @return
     */
    QueryWrapper<Complaint> getQueryWrapper(ComplaintQueryRequest complaintQueryRequest,  User logonUser);



    /**
     * 分页获取投诉列表封装
     *
     * @param complaintPage
     * @param request
     * @return
     */
    Page<ComplaintVO> getComplaintVOPage(Page<Complaint> complaintPage, HttpServletRequest request);
}
