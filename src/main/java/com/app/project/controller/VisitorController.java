package com.app.project.controller;

import com.app.project.annotation.AuthCheck;
import com.app.project.common.BaseResponse;
import com.app.project.common.DeleteRequest;
import com.app.project.common.ErrorCode;
import com.app.project.common.ResultUtils;
import com.app.project.constant.UserConstant;
import com.app.project.exception.BusinessException;
import com.app.project.exception.ThrowUtils;
import com.app.project.model.dto.visitor.VisitorAddRequest;
import com.app.project.model.dto.visitor.VisitorEditRequest;
import com.app.project.model.dto.visitor.VisitorQueryRequest;
import com.app.project.model.dto.visitor.VisitorUpdateRequest;
import com.app.project.model.entity.User;
import com.app.project.model.entity.Visitor;
import com.app.project.model.vo.VisitorVO;
import com.app.project.service.UserService;
import com.app.project.service.VisitorService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 访客接口
 *
 * @author
 * @from
 */
@RestController
@RequestMapping("/visitor")
@Slf4j
public class VisitorController {

    @Resource
    private VisitorService visitorService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建访客
     *
     * @param visitorAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addVisitor(@Valid @RequestBody VisitorAddRequest visitorAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(visitorAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Visitor visitor = new Visitor();
        BeanUtils.copyProperties(visitorAddRequest, visitor);

        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        visitor.setUserId(loginUser.getId());

        // 写入数据库
        boolean result = visitorService.save(visitor);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newVisitorId = visitor.getId();
        return ResultUtils.success(newVisitorId);
    }

    /**
     * 删除访客
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteVisitor(@Valid @RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Visitor oldVisitor = visitorService.getById(id);
        ThrowUtils.throwIf(oldVisitor == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人可删除
        if (!oldVisitor.getUserId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = visitorService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新访客（仅管理员可用）
     *
     * @param visitorUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateVisitor(@Valid @RequestBody VisitorUpdateRequest visitorUpdateRequest) {
        if (visitorUpdateRequest == null || visitorUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Visitor visitor = new Visitor();
        BeanUtils.copyProperties(visitorUpdateRequest, visitor);

        // 判断是否存在
        long id = visitorUpdateRequest.getId();
        Visitor oldVisitor = visitorService.getById(id);
        ThrowUtils.throwIf(oldVisitor == null, ErrorCode.NOT_FOUND_ERROR);

        // 只能处理待审核的登记
        Integer oldVisitorStatus = oldVisitor.getStatus();
        ThrowUtils.throwIf(!oldVisitorStatus.equals(0), ErrorCode.OPERATION_ERROR, "该访客登记已审核，请勿重复操作");
        // 操作数据库
        boolean result = visitorService.updateById(visitor);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }


    /**
     * 分页获取访客列表（封装类）
     *
     * @param visitorQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<VisitorVO>> listVisitorVOByPage( @RequestBody VisitorQueryRequest visitorQueryRequest,
                                                               HttpServletRequest request) {
        long current = visitorQueryRequest.getCurrent();
        long size = visitorQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
          User loginUser = userService.getLoginUser(request);
        Page<Visitor> visitorPage = visitorService.page(new Page<>(current, size),
                visitorService.getQueryWrapper(visitorQueryRequest,loginUser));
        // 获取封装类
        return ResultUtils.success(visitorService.getVisitorVOPage(visitorPage, request));
    }


    /**
     * 编辑访客（给用户使用）
     *
     * @param visitorEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editVisitor(@Valid @RequestBody VisitorEditRequest visitorEditRequest, HttpServletRequest request) {
        if (visitorEditRequest == null || visitorEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Visitor visitor = new Visitor();
        BeanUtils.copyProperties(visitorEditRequest, visitor);

        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = visitorEditRequest.getId();
        Visitor oldVisitor = visitorService.getById(id);
        ThrowUtils.throwIf(oldVisitor == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldVisitor.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 只能编辑待审核的登记
        Integer oldVisitorStatus = oldVisitor.getStatus();
        ThrowUtils.throwIf(!oldVisitorStatus.equals(0), ErrorCode.OPERATION_ERROR, "该访客登记已审核，不可修改");

        // 操作数据库
        boolean result = visitorService.updateById(visitor);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
