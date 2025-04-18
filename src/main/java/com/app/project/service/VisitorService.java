package com.app.project.service;


import com.app.project.model.dto.visitor.VisitorQueryRequest;
import com.app.project.model.entity.User;
import com.app.project.model.entity.Visitor;
import com.app.project.model.vo.VisitorVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author Administrator
* @description 针对表【visitor(访客登记)】的数据库操作Service
* @createDate 2025-04-18 19:21:46
*/
public interface VisitorService extends IService<Visitor> {

    /**
     * 获取查询条件
     * @param visitorQueryRequest
     * @param loginUser
     * @return
     */
    QueryWrapper<Visitor> getQueryWrapper(VisitorQueryRequest visitorQueryRequest, User loginUser);

    /**
     * 分页获取访客列表封装
     * @param visitorPage
     * @param request
     * @return
     */
    Page<VisitorVO> getVisitorVOPage(Page<Visitor> visitorPage, HttpServletRequest request);
}
