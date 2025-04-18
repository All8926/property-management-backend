package com.app.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.app.project.constant.CommonConstant;
import com.app.project.constant.UserConstant;
import com.app.project.mapper.VisitorMapper;
import com.app.project.model.dto.visitor.VisitorQueryRequest;
import com.app.project.model.entity.User;
import com.app.project.model.entity.Visitor;
import com.app.project.model.vo.VisitorVO;
import com.app.project.service.UserService;
import com.app.project.service.VisitorService;
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
* @description 针对表【visitor(访客登记)】的数据库操作Service实现
* @createDate 2025-04-18 19:21:46
*/
@Service
public class VisitorServiceImpl extends ServiceImpl<VisitorMapper, Visitor>
    implements VisitorService {
    
    @Resource
    private UserService userService;

    @Override
    public QueryWrapper<Visitor> getQueryWrapper(VisitorQueryRequest visitorQueryRequest, User loginUser) {
        QueryWrapper<Visitor> queryWrapper = new QueryWrapper<>();
        if (visitorQueryRequest == null) {
            return queryWrapper;
        }
        // 取值
        Long id = visitorQueryRequest.getId();
        String visitorName = visitorQueryRequest.getVisitorName();
        String visitorPhone = visitorQueryRequest.getVisitorPhone();
        Date visitingTime = visitorQueryRequest.getVisitingTime();
        Integer status = visitorQueryRequest.getStatus();

        String sortField = visitorQueryRequest.getSortField();
        String sortOrder = visitorQueryRequest.getSortOrder();

        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(visitorName), "visitorName", visitorName);
        queryWrapper.like(StringUtils.isNotBlank(visitorPhone), "visitorPhone", visitorPhone); 


        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);

       // 小于等于来访时间
        queryWrapper.le(ObjectUtils.isNotEmpty(visitingTime), "visitingTime", visitingTime);


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
    public Page<VisitorVO> getVisitorVOPage(Page<Visitor> visitorPage, HttpServletRequest request) {
        List<Visitor> visitorList = visitorPage.getRecords();
        Page<VisitorVO> visitorVOPage = new Page<>(visitorPage.getCurrent(), visitorPage.getSize(), visitorPage.getTotal());
        if (CollUtil.isEmpty(visitorList)) {
            return visitorVOPage;
        }
        // Visitor => VisitorVO
        List<VisitorVO> visitorVOList = visitorList.stream().map(visitor -> {
            VisitorVO visitorVO = new VisitorVO();
            BeanUtils.copyProperties(visitor, visitorVO);
            return visitorVO;
        }).collect(Collectors.toList());

        // 1. 关联查询用户信息
        Set<Long> userIdSet = visitorList.stream().map(Visitor::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));

        // 填充信息
        visitorVOList.forEach(visitorVO -> {
            Long userId = visitorVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            visitorVO.setUser(userService.getUserVO(user));
        });
        // endregion

        visitorVOPage.setRecords(visitorVOList);
        return visitorVOPage;
    }
}




