package com.app.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.app.project.annotation.AuthCheck;
import com.app.project.common.BaseResponse;
import com.app.project.common.DeleteRequest;
import com.app.project.common.ErrorCode;
import com.app.project.common.ResultUtils;
import com.app.project.constant.UserConstant;
import com.app.project.exception.BusinessException;
import com.app.project.exception.ThrowUtils;
import com.app.project.model.dto.paymentItem.PaymentItemAddRequest;
import com.app.project.model.dto.paymentItem.PaymentItemQueryRequest;
import com.app.project.model.dto.paymentItem.PaymentItemUpdateRequest;
import com.app.project.model.entity.PaymentItem;
import com.app.project.model.entity.User;
import com.app.project.model.vo.PaymentItemVO;
import com.app.project.service.PaymentItemService;
import com.app.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 缴费项目接口
 *
 * @author
 * @from
 */
@RestController
@RequestMapping("/paymentItem")
@Slf4j
public class PaymentItemController {

    @Resource
    private PaymentItemService paymentItemService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建缴费项目（仅管理员可用）
     *
     * @param paymentItemAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addPaymentItem(@Valid @RequestBody PaymentItemAddRequest paymentItemAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(paymentItemAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        PaymentItem paymentItem = new PaymentItem();
        BeanUtils.copyProperties(paymentItemAddRequest, paymentItem);

        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        paymentItem.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = paymentItemService.save(paymentItem);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newPaymentItemId = paymentItem.getId();
        return ResultUtils.success(newPaymentItemId);
    }

    /**
     * 删除缴费项目（仅管理员可用）
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deletePaymentItem(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        // 判断是否存在
        PaymentItem oldPaymentItem = paymentItemService.getById(id);
        ThrowUtils.throwIf(oldPaymentItem == null, ErrorCode.NOT_FOUND_ERROR);

        // 操作数据库
        boolean result = paymentItemService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新缴费项目（仅管理员可用）
     *
     * @param paymentItemUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updatePaymentItem(@Valid @RequestBody PaymentItemUpdateRequest paymentItemUpdateRequest) {
        if (paymentItemUpdateRequest == null || paymentItemUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        PaymentItem paymentItem = new PaymentItem();
        BeanUtils.copyProperties(paymentItemUpdateRequest, paymentItem);

        // 判断是否存在
        long id = paymentItemUpdateRequest.getId();
        PaymentItem oldPaymentItem = paymentItemService.getById(id);
        ThrowUtils.throwIf(oldPaymentItem == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = paymentItemService.updateById(paymentItem);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }


    /**
     * 分页获取缴费项目列表（仅管理员可用）
     *
     * @param paymentItemQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<PaymentItem>> listPaymentItemByPage(@RequestBody PaymentItemQueryRequest paymentItemQueryRequest) {
        long current = paymentItemQueryRequest.getCurrent();
        long size = paymentItemQueryRequest.getPageSize();
        // 查询数据库
        Page<PaymentItem> paymentItemPage = paymentItemService.page(new Page<>(current, size),
                paymentItemService.getQueryWrapper(paymentItemQueryRequest));
        return ResultUtils.success(paymentItemPage);
    }

    /**
     * 分页获取缴费项目列表（封装类）
     *
     * @param paymentItemQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<PaymentItemVO>> listPaymentItemVOByPage(@RequestBody PaymentItemQueryRequest paymentItemQueryRequest,
                                                               HttpServletRequest request) {
        long current = paymentItemQueryRequest.getCurrent();
        long size = paymentItemQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<PaymentItem> paymentItemPage = paymentItemService.page(new Page<>(current, size),
                paymentItemService.getQueryWrapper(paymentItemQueryRequest));
        // 获取封装类
        return ResultUtils.success(paymentItemService.getPaymentItemVOPage(paymentItemPage, request));
    }




    // endregion
}
