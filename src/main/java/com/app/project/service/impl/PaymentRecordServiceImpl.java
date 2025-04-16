package com.app.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.app.project.common.ErrorCode;
import com.app.project.constant.CommonConstant;
import com.app.project.constant.UserConstant;
import com.app.project.exception.ThrowUtils;
import com.app.project.mapper.PaymentRecordMapper;
import com.app.project.model.dto.paymentItem.PaymentItemQueryRequest;
import com.app.project.model.dto.paymentRecord.PaymentRecordAddRequest;
import com.app.project.model.dto.paymentRecord.PaymentRecordQueryRequest;
import com.app.project.model.entity.PaymentItem;
import com.app.project.model.entity.PaymentRecord;
import com.app.project.model.entity.User;
import com.app.project.model.vo.PaymentItemVO;
import com.app.project.model.vo.PaymentRecordVO;
import com.app.project.service.PaymentItemService;
import com.app.project.service.PaymentRecordService;

import com.app.project.service.UserService;
import com.app.project.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【payment_record(缴费项目)】的数据库操作Service实现
* @createDate 2025-04-16 20:00:15
*/
@Service
public class PaymentRecordServiceImpl extends ServiceImpl<PaymentRecordMapper, PaymentRecord>
    implements PaymentRecordService {
    
    @Resource
    private PaymentItemService paymentItemService;

    @Resource
    private UserService userService;
    
    
    /**
     * 获取查询条件
     *
     * @param paymentRecordQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<PaymentRecord> getQueryWrapper(PaymentRecordQueryRequest paymentRecordQueryRequest, User logonUser) {
        QueryWrapper<PaymentRecord> queryWrapper = new QueryWrapper<>();
        if (paymentRecordQueryRequest == null) {
            return queryWrapper;
        }
        // 取值
        Long id = paymentRecordQueryRequest.getId();
        String paymentName = paymentRecordQueryRequest.getPaymentName();
        String userName = paymentRecordQueryRequest.getUserName();
        Date payDate = paymentRecordQueryRequest.getPayDate();
  
        String sortField = paymentRecordQueryRequest.getSortField();
        String sortOrder = paymentRecordQueryRequest.getSortOrder();

        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(paymentName), "paymentName", paymentName);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName); 

        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);  
        // 小于等于过期时间
        queryWrapper.le(ObjectUtils.isNotEmpty(payDate), "payDate", payDate);
        
        // 非管理员只能获取自己的
        String userRole = logonUser.getUserRole();
        if (!userRole.equals(UserConstant.ADMIN_ROLE)) {
            queryWrapper.eq("userId", logonUser.getId());
        }

        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Boolean addPaymentRecord(PaymentRecordAddRequest paymentRecordAddRequest, User loginUser) {
        long paymentId = paymentRecordAddRequest.getPaymentId();
        // 1.检查项目是否存在
        PaymentItem paymentItem = paymentItemService.getById(paymentId);
        ThrowUtils.throwIf(ObjectUtils.isEmpty(paymentItem), ErrorCode.PARAMS_ERROR, "项目不存在");

        // 2. 校验该项目是否已缴费
        QueryWrapper<PaymentRecord> paymentRecordQueryWrapper = new QueryWrapper<>();
        paymentRecordQueryWrapper.eq("paymentId",paymentId);
        paymentRecordQueryWrapper.eq("userId", loginUser.getId());
        PaymentRecord paymentRecord = this.getOne(paymentRecordQueryWrapper);
        ThrowUtils.throwIf(ObjectUtils.isNotEmpty(paymentRecord), ErrorCode.PARAMS_ERROR, "该项目已缴费");

        // 3. 校验项目是否已过期
        ThrowUtils.throwIf(paymentItem.getExpirationTime().before(new Date()), ErrorCode.PARAMS_ERROR, "该项目已过期");
        
        // 4. 插入数据
        paymentRecord = new PaymentRecord();
        paymentRecord.setPaymentId(paymentRecordAddRequest.getPaymentId());
        paymentRecord.setUserId(loginUser.getId());
        paymentRecord.setPayAmount(paymentItem.getAmount());
        paymentRecord.setPayDate(new Date());
        paymentRecord.setUserName(loginUser.getUserName());
        paymentRecord.setPaymentName(paymentItem.getName());
        boolean result = this.save(paymentRecord);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public Page<PaymentRecordVO> getPaymentRecordVOPage(Page<PaymentRecord> paymentRecordPage, HttpServletRequest request) {
        List<PaymentRecord> paymentRecordList = paymentRecordPage.getRecords();
        Page<PaymentRecordVO> paymentRecordVOPage = new Page<>(paymentRecordPage.getCurrent(), paymentRecordPage.getSize(), paymentRecordPage.getTotal());
        if (CollUtil.isEmpty(paymentRecordList)) {
            return paymentRecordVOPage;
        }
        // paymentRecord => paymentRecordVO
        List<PaymentRecordVO> paymentRecordVOList = paymentRecordList.stream().map(paymentRecord -> {
            PaymentRecordVO paymentRecordVO = new PaymentRecordVO();
            BeanUtils.copyProperties(paymentRecord, paymentRecordVO);
            return paymentRecordVO;
        }).collect(Collectors.toList());


        // endregion

        paymentRecordVOPage.setRecords(paymentRecordVOList);
        return paymentRecordVOPage;
    }
}




