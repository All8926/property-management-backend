package com.app.project.service;


import com.app.project.model.dto.repairs.RepairsCommentRequest;
import com.app.project.model.dto.repairs.RepairsQueryRequest;
import com.app.project.model.dto.repairs.RepairsUpdateRequest;
import com.app.project.model.entity.Repairs;
import com.app.project.model.entity.Repairs;
import com.app.project.model.entity.User;
import com.app.project.model.vo.RepairsVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author Administrator
* @description 针对表【repairs(报修记录)】的数据库操作Service
* @createDate 2025-04-18 09:30:50
*/
public interface RepairsService extends IService<Repairs> {
    /**
     * 获取查询条件
     *
     * @param repairsQueryRequest
     * @return
     */
    QueryWrapper<Repairs> getQueryWrapper(RepairsQueryRequest repairsQueryRequest, User logonUser);



    /**
     * 分页获取报修列表封装
     *
     * @param repairsPage
     * @param request
     * @return
     */
    Page<RepairsVO> getRepairsVOPage(Page<Repairs> repairsPage, HttpServletRequest request);

    /**
     * 更新报修记录
     * @param repairsUpdateRequest
     * @param loginUser
     * @return
     */
    Boolean updateRepairs(RepairsUpdateRequest repairsUpdateRequest, User loginUser);

    /**
     * 添加评价
     * @param repairsCommentRequest
     * @param loginUser
     * @return
     */
    Boolean addComment(RepairsCommentRequest repairsCommentRequest, User loginUser);
}
