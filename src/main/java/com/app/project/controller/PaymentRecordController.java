package com.app.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.app.project.common.BaseResponse;
import com.app.project.common.ErrorCode;
import com.app.project.common.ResultUtils;
import com.app.project.exception.ThrowUtils;
import com.app.project.model.dto.paymentRecord.PaymentRecordAddRequest;
import com.app.project.model.dto.paymentRecord.PaymentRecordQueryRequest;
import com.app.project.model.entity.PaymentRecord;
import com.app.project.model.entity.User;
import com.app.project.model.vo.PaymentRecordVO;
import com.app.project.service.PaymentRecordService;
import com.app.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 缴费记录接口
 *
 * @author
 * @from
 */
@RestController
@RequestMapping("/paymentRecord")
@Slf4j
public class PaymentRecordController {

    @Resource
    private PaymentRecordService paymentRecordService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建缴费记录
     *
     * @param paymentRecordAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> addPaymentRecord(@Valid @RequestBody PaymentRecordAddRequest paymentRecordAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(paymentRecordAddRequest == null, ErrorCode.PARAMS_ERROR);

        User loginUser = userService.getLoginUser(request);
       Boolean result =  paymentRecordService.addPaymentRecord(paymentRecordAddRequest, loginUser);
        return ResultUtils.success(result);
    }


    /**
     * 分页获取缴费记录列表（封装类）
     *
     * @param paymentRecordQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<PaymentRecordVO>> listPaymentRecordVOByPage(@RequestBody PaymentRecordQueryRequest paymentRecordQueryRequest,
                                                               HttpServletRequest request) {
        long current = paymentRecordQueryRequest.getCurrent();
        long size = paymentRecordQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        User loginUser = userService.getLoginUser(request);
        Page<PaymentRecord> paymentRecordPage = paymentRecordService.page(new Page<>(current, size),
                paymentRecordService.getQueryWrapper(paymentRecordQueryRequest,loginUser));
        // 获取封装类
        return ResultUtils.success(paymentRecordService.getPaymentRecordVOPage(paymentRecordPage, request));
    }

    // endregion
}
