package com.app.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.app.project.model.dto.paymentItem.PaymentItemQueryRequest;
import com.app.project.model.entity.PaymentItem;
import com.app.project.model.vo.PaymentItemVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 缴费项目服务
 *
 * @author
 * @from
 */
public interface PaymentItemService extends IService<PaymentItem> {



    /**
     * 获取查询条件
     *
     * @param paymentItemQueryRequest
     * @return
     */
    QueryWrapper<PaymentItem> getQueryWrapper(PaymentItemQueryRequest paymentItemQueryRequest);
    


    /**
     * 分页获取缴费项目封装
     *
     * @param paymentItemPage
     * @param request
     * @return
     */
    Page<PaymentItemVO> getPaymentItemVOPage(Page<PaymentItem> paymentItemPage, HttpServletRequest request);
}
