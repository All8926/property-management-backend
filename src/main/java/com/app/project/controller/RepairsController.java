package com.app.project.controller;

import cn.hutool.json.JSONUtil;
import com.app.project.annotation.AuthCheck;
import com.app.project.common.BaseResponse;
import com.app.project.common.DeleteRequest;
import com.app.project.common.ErrorCode;
import com.app.project.common.ResultUtils;
import com.app.project.constant.UserConstant;
import com.app.project.exception.BusinessException;
import com.app.project.exception.ThrowUtils;
import com.app.project.model.dto.repairs.*;
import com.app.project.model.entity.Repairs;
import com.app.project.model.entity.User;
import com.app.project.model.vo.RepairsVO;
import com.app.project.service.RepairsService;
import com.app.project.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
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
import java.util.List;

/**
 * 报修接口
 *
 * @author
 * @from
 */
@RestController
@RequestMapping("/repairs")
@Slf4j
public class RepairsController {

    @Resource
    private RepairsService repairsService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建报修
     *
     * @param repairsAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addRepairs(@Valid @RequestBody RepairsAddRequest repairsAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(repairsAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Repairs repairs = new Repairs();
        BeanUtils.copyProperties(repairsAddRequest, repairs);

        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        repairs.setUserId(loginUser.getId());
        List<String> image = repairsAddRequest.getImageList();
        repairs.setImage(JSONUtil.toJsonStr(image));

        // 写入数据库
        boolean result = repairsService.save(repairs);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newRepairsId = repairs.getId();
        return ResultUtils.success(newRepairsId);
    }

    /**
     * 删除报修
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteRepairs(@Valid @RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Repairs oldRepairs = repairsService.getById(id);
        ThrowUtils.throwIf(oldRepairs == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人可删除
        if (!oldRepairs.getUserId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = repairsService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新报修（仅管理员、维修员可用）
     *
     * @param repairsUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRoles = {UserConstant.ADMIN_ROLE, UserConstant.SERVICEMAN_ROLE})
    public BaseResponse<Boolean> updateRepairs(@Valid @RequestBody RepairsUpdateRequest repairsUpdateRequest, HttpServletRequest request) {
        if (repairsUpdateRequest == null || repairsUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Boolean result = repairsService.updateRepairs(repairsUpdateRequest, loginUser);
        return ResultUtils.success(result);
    }


    /**
     * 分页获取报修列表（封装类）
     *
     * @param repairsQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<RepairsVO>> listRepairsVOByPage(@RequestBody RepairsQueryRequest repairsQueryRequest,
                                                             HttpServletRequest request) {
        long current = repairsQueryRequest.getCurrent();
        long size = repairsQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        User loginUser = userService.getLoginUser(request);
        Page<Repairs> repairsPage = repairsService.page(new Page<>(current, size),
                repairsService.getQueryWrapper(repairsQueryRequest, loginUser));
        // 获取封装类
        return ResultUtils.success(repairsService.getRepairsVOPage(repairsPage, request));
    }


    /**
     * 编辑报修（给用户使用）
     *
     * @param repairsEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editRepairs(@Valid @RequestBody RepairsEditRequest repairsEditRequest, HttpServletRequest request) {
        if (repairsEditRequest == null || repairsEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Repairs repairs = new Repairs();
        BeanUtils.copyProperties(repairsEditRequest, repairs);
        List<String> image = repairsEditRequest.getImageList();
        if (image != null) {
            repairs.setImage(JSONUtil.toJsonStr(image));
        }


        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = repairsEditRequest.getId();
        Repairs oldRepairs = repairsService.getById(id);
        ThrowUtils.throwIf(oldRepairs == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldRepairs.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = repairsService.updateById(repairs);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @PostMapping("/comment/add")
    public BaseResponse<Boolean> addComment(@Valid @RequestBody RepairsCommentRequest repairsCommentRequest, HttpServletRequest request) {
        final User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(repairsService.addComment(repairsCommentRequest, loginUser));
    }

    // endregion
}
