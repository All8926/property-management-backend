package com.app.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.app.project.constant.CommonConstant;
import com.app.project.constant.UserConstant;
import com.app.project.mapper.ComplaintMapper;
import com.app.project.model.dto.complaint.ComplaintQueryRequest;
import com.app.project.model.entity.Complaint;
import com.app.project.model.entity.User;
import com.app.project.model.vo.ComplaintVO;
import com.app.project.service.ComplaintService;
import com.app.project.service.UserService;
import com.app.project.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【complaint(投诉记录)】的数据库操作Service实现
 * @createDate 2025-04-16 21:44:19
 */
@Service
public class ComplaintServiceImpl extends ServiceImpl<ComplaintMapper, Complaint>
        implements ComplaintService {

    @Resource
    private UserService userService;

    /**
     * 获取查询条件
     *
     * @param complaintQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Complaint> getQueryWrapper(ComplaintQueryRequest complaintQueryRequest, User loginUser) {
        QueryWrapper<Complaint> queryWrapper = new QueryWrapper<>();
        if (complaintQueryRequest == null) {
            return queryWrapper;
        }
        // 取值
        Long id = complaintQueryRequest.getId();
        String title = complaintQueryRequest.getTitle();
        Integer status = complaintQueryRequest.getStatus();

        String sortField = complaintQueryRequest.getSortField();
        String sortOrder = complaintQueryRequest.getSortOrder();

        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);

        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);


        // 非管理员只能获取自己的
        String userRole = loginUser.getUserRole();
        if (!userRole.equals(UserConstant.ADMIN_ROLE)) {
            queryWrapper.eq("userId", loginUser.getId());
        }

        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<ComplaintVO> getComplaintVOPage(Page<Complaint> complaintPage, HttpServletRequest request) {
        List<Complaint> complaintList = complaintPage.getRecords();
        Page<ComplaintVO> complaintVOPage = new Page<>(complaintPage.getCurrent(), complaintPage.getSize(), complaintPage.getTotal());
        if (CollUtil.isEmpty(complaintList)) {
            return complaintVOPage;
        }
        // Complaint => ComplaintVO
        List<ComplaintVO> complaintVOList = complaintList.stream().map(ComplaintVO::objToVo).collect(Collectors.toList());

        // 1. 关联查询用户信息
        Set<Long> userIdSet = complaintList.stream().map(Complaint::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));

        // 填充信息
        complaintVOList.forEach(complaintVO -> {
            Long userId = complaintVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            complaintVO.setUser(userService.getUserVO(user));
        });
        // endregion

        complaintVOPage.setRecords(complaintVOList);
        return complaintVOPage;
    }

}




