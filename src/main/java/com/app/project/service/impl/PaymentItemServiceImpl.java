package com.app.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.app.project.constant.CommonConstant;
import com.app.project.mapper.PaymentItemMapper;
import com.app.project.model.dto.paymentItem.PaymentItemQueryRequest;
import com.app.project.model.entity.PaymentItem;
import com.app.project.model.entity.User;
import com.app.project.model.vo.PaymentItemVO;
import com.app.project.service.PaymentItemService;
import com.app.project.service.UserService;
import com.app.project.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 缴费项目服务实现
 *
 * @author
 * @from
 */
@Service
@Slf4j
public class PaymentItemServiceImpl extends ServiceImpl<PaymentItemMapper, PaymentItem> implements PaymentItemService {

    @Resource
    private UserService userService;


    /**
     * 获取查询条件
     *
     * @param paymentItemQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<PaymentItem> getQueryWrapper(PaymentItemQueryRequest paymentItemQueryRequest) {
        QueryWrapper<PaymentItem> queryWrapper = new QueryWrapper<>();
        if (paymentItemQueryRequest == null) {
            return queryWrapper;
        }
        // 取值
        Long id = paymentItemQueryRequest.getId();
        String name = paymentItemQueryRequest.getName();
        String amount = paymentItemQueryRequest.getAmount();
        String profile = paymentItemQueryRequest.getProfile();
        Date expirationTime = paymentItemQueryRequest.getExpirationTime();
        Long userId = paymentItemQueryRequest.getUserId();
        String sortField = paymentItemQueryRequest.getSortField();
        String sortOrder = paymentItemQueryRequest.getSortOrder();

        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(amount), "amount", amount);
        queryWrapper.like(StringUtils.isNotBlank(profile), "profile", profile);

        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 小于等于过期时间
        queryWrapper.le(ObjectUtils.isNotEmpty(expirationTime), "expirationTime", expirationTime);

        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 分页获取缴费项目封装
     *
     * @param paymentItemPage
     * @param request
     * @return
     */
    @Override
    public Page<PaymentItemVO> getPaymentItemVOPage(Page<PaymentItem> paymentItemPage, HttpServletRequest request) {
        List<PaymentItem> paymentItemList = paymentItemPage.getRecords();
        Page<PaymentItemVO> paymentItemVOPage = new Page<>(paymentItemPage.getCurrent(), paymentItemPage.getSize(), paymentItemPage.getTotal());
        if (CollUtil.isEmpty(paymentItemList)) {
            return paymentItemVOPage;
        }
        // PaymentItem => PaymentItemVO
        List<PaymentItemVO> paymentItemVOList = paymentItemList.stream().map(paymentItem -> {
            PaymentItemVO paymentItemVO = new PaymentItemVO();
            BeanUtils.copyProperties(paymentItem, paymentItemVO);
            return paymentItemVO;
        }).collect(Collectors.toList());

        // 1. 关联查询用户信息
        Set<Long> userIdSet = paymentItemList.stream().map(PaymentItem::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));

        // 填充信息
        paymentItemVOList.forEach(paymentItemVO -> {
            Long userId = paymentItemVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            paymentItemVO.setCreateUser(userService.getUserVO(user));
        });
        // endregion

        paymentItemVOPage.setRecords(paymentItemVOList);
        return paymentItemVOPage;
    }

}
