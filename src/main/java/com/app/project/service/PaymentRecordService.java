package com.app.project.service;

import com.app.project.model.dto.paymentItem.PaymentItemQueryRequest;
import com.app.project.model.dto.paymentRecord.PaymentRecordAddRequest;
import com.app.project.model.dto.paymentRecord.PaymentRecordQueryRequest;
import com.app.project.model.entity.PaymentItem;
import com.app.project.model.entity.PaymentRecord;
import com.app.project.model.entity.User;
import com.app.project.model.vo.PaymentRecordVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author Administrator
* @description 针对表【payment_record(缴费项目)】的数据库操作Service
* @createDate 2025-04-16 20:00:15
*/
public interface PaymentRecordService extends IService<PaymentRecord> {

    /**
     * 获取查询条件
     *
     * @param paymentItemQueryRequest
     * @return
     */
    QueryWrapper<PaymentRecord> getQueryWrapper(PaymentRecordQueryRequest paymentRecordQueryRequest, User logonUser);


    /**
     * 添加缴费记录
     * @param paymentRecordAddRequest
     * @param loginUser
     * @return
     */
    Boolean addPaymentRecord(PaymentRecordAddRequest paymentRecordAddRequest, User loginUser);

    /**
     * 获取缴费记录分页
     * @param paymentRecordPage
     * @param request
     * @return
     */
    Page<PaymentRecordVO> getPaymentRecordVOPage(Page<PaymentRecord> paymentRecordPage, HttpServletRequest request);
}
