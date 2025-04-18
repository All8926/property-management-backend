package com.app.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.app.project.common.ErrorCode;
import com.app.project.constant.CommonConstant;
import com.app.project.constant.UserConstant;
import com.app.project.exception.BusinessException;
import com.app.project.exception.ThrowUtils;
import com.app.project.mapper.RepairsMapper;
import com.app.project.model.dto.repairs.RepairsCommentRequest;
import com.app.project.model.dto.repairs.RepairsQueryRequest;
import com.app.project.model.dto.repairs.RepairsUpdateRequest;
import com.app.project.model.entity.Repairs;
import com.app.project.model.entity.Repairs;
import com.app.project.model.entity.User;
import com.app.project.model.enums.RepairsStatusEnum;
import com.app.project.model.vo.RepairsVO;
import com.app.project.service.RepairsService;

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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【repairs(报修记录)】的数据库操作Service实现
 * @createDate 2025-04-18 09:30:50
 */
@Service
public class RepairsServiceImpl extends ServiceImpl<RepairsMapper, Repairs>
        implements RepairsService {
    @Resource
    private UserService userService;

    /**
     * 获取查询条件
     *
     * @param repairsQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Repairs> getQueryWrapper(RepairsQueryRequest repairsQueryRequest, User logonUser) {
        QueryWrapper<Repairs> queryWrapper = new QueryWrapper<>();
        if (repairsQueryRequest == null) {
            return queryWrapper;
        }
        // 取值
        Long id = repairsQueryRequest.getId();
        String title = repairsQueryRequest.getTitle();
        Integer status = repairsQueryRequest.getStatus();
        Long servicemanId = repairsQueryRequest.getServicemanId();

        String sortField = repairsQueryRequest.getSortField();
        String sortOrder = repairsQueryRequest.getSortOrder();

        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);

        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(servicemanId), "servicemanId", servicemanId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);


        // 普通用户只能获取自己的
        String userRole = logonUser.getUserRole();
        Long userId = logonUser.getId();
        if (userRole.equals(UserConstant.DEFAULT_ROLE)) {
            queryWrapper.eq("userId", userId);
        }

        // 维修员获取自己的或者自己维修的
        if (userRole.equals(UserConstant.SERVICEMAN_ROLE)) {
            queryWrapper.and(wrapper -> {
                // 当 servicemanId 不为空时，添加 servicemanId 条件
                wrapper.eq("servicemanId", userId);
                // 当 userId 不为空时，添加 userId 条件，并与 servicemanId 条件形成 "或" 关系
                wrapper.or(); // 添加 "或" 操作
                wrapper.eq("userId", userId);
            });
        }

        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<RepairsVO> getRepairsVOPage(Page<Repairs> repairsPage, HttpServletRequest request) {
        List<Repairs> repairsList = repairsPage.getRecords();
        Page<RepairsVO> repairsVOPage = new Page<>(repairsPage.getCurrent(), repairsPage.getSize(), repairsPage.getTotal());
        if (CollUtil.isEmpty(repairsList)) {
            return repairsVOPage;
        }

        // paymentRecord => paymentRecordVO
        List<RepairsVO> repairsVOList = repairsList.stream().map(RepairsVO::objToVo).collect(Collectors.toList());

        // 1. 关联查询用户信息和维修人员信息
        Set<Long> userIdSet = repairsList.stream().map(Repairs::getUserId).collect(Collectors.toSet());
        Set<Long> serviceIdSet = repairsList.stream().map(Repairs::getServicemanId).filter(ObjectUtils::isNotEmpty).collect(Collectors.toSet());

        // 合并查询
        Set<Long> allIds = new HashSet<>(userIdSet);
        allIds.addAll(serviceIdSet);

        // 如果 allIds 为空，则无需查询
        if (!CollUtil.isEmpty(allIds)) {
            Map<Long, List<User>> allUsersMap = userService.listByIds(allIds).stream()
                    .collect(Collectors.groupingBy(User::getId));

            // 填充信息
            repairsVOList.forEach(repairsVO -> {
                Long userId = repairsVO.getUserId();
                if (userId != null && allUsersMap.containsKey(userId)) {
                    User user = allUsersMap.get(userId).get(0);
                    repairsVO.setUser(userService.getUserVO(user));
                }

                Long repairId = repairsVO.getServicemanId();
                if (repairId != null && allUsersMap.containsKey(repairId)) {
                    User user = allUsersMap.get(repairId).get(0);
                    repairsVO.setServicemanUser(userService.getUserVO(user));
                }
            });
        }

        repairsVOPage.setRecords(repairsVOList);
        return repairsVOPage;
    }

    @Override
    public Boolean updateRepairs(RepairsUpdateRequest repairsUpdateRequest, User loginUser) {
        Repairs repairs = new Repairs();
        BeanUtils.copyProperties(repairsUpdateRequest, repairs);
        // 判断是否存在
        long id = repairsUpdateRequest.getId();
        Repairs oldRepairs = this.getById(id);
        ThrowUtils.throwIf(oldRepairs == null, ErrorCode.NOT_FOUND_ERROR);
        int status = oldRepairs.getStatus();
        String userRole = loginUser.getUserRole();
        Integer requestStatus = repairsUpdateRequest.getStatus();
        // 管理员只能操作驳回、维修中状态
        if (userRole.equals(UserConstant.ADMIN_ROLE)) {
            if (status != RepairsStatusEnum.IN_REVIEW.getValue()) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "该报修状态未在审核中");
            }
            if (!requestStatus.equals(RepairsStatusEnum.REJECTED.getValue()) && !requestStatus.equals(RepairsStatusEnum.UNDER_REPAIR.getValue())) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
        // 维修员只能操作维修失败、已完成状态
        if (userRole.equals(UserConstant.SERVICEMAN_ROLE)) {
            if (status != RepairsStatusEnum.UNDER_REPAIR.getValue()) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "该报修状态未在维修中");
            }
            if (!requestStatus.equals(RepairsStatusEnum.PENDING_EVALUATION.getValue()) && !requestStatus.equals(RepairsStatusEnum.UNREPAIRABLE.getValue())) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
        // 操作数据库
        boolean result = this.updateById(repairs);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return null;
    }

    @Override
    public Boolean addComment(RepairsCommentRequest repairsCommentRequest, User loginUser) {
        long userId = loginUser.getId();
        final Repairs oldRepairs = this.getById(repairsCommentRequest.getId());
        ThrowUtils.throwIf(oldRepairs == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人可评论
        ThrowUtils.throwIf(userId != oldRepairs.getUserId(), ErrorCode.NO_AUTH_ERROR);
        // 仅在待评价状态可评论
        ThrowUtils.throwIf(!oldRepairs.getStatus().equals(RepairsStatusEnum.PENDING_EVALUATION.getValue()) , ErrorCode.OPERATION_ERROR, "该报修未完成");
        oldRepairs.setComment(repairsCommentRequest.getComment());
        oldRepairs.setStatus(RepairsStatusEnum.COMPLETED.getValue());
        boolean result = this.updateById(oldRepairs);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }

}




