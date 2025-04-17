package com.app.project.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.app.project.annotation.AuthCheck;
import com.app.project.common.BaseResponse;
import com.app.project.common.DeleteRequest;
import com.app.project.common.ErrorCode;
import com.app.project.common.ResultUtils;
import com.app.project.constant.UserConstant;
import com.app.project.exception.BusinessException;
import com.app.project.exception.ThrowUtils;
import com.app.project.model.dto.complaint.ComplaintAddRequest;
import com.app.project.model.dto.complaint.ComplaintEditRequest;
import com.app.project.model.dto.complaint.ComplaintQueryRequest;
import com.app.project.model.dto.complaint.ComplaintUpdateRequest;
import com.app.project.model.entity.Complaint;
import com.app.project.model.entity.User;
import com.app.project.model.vo.ComplaintVO;
import com.app.project.service.ComplaintService;
import com.app.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 投诉接口
 *
 * @author
 * @from
 */
@RestController
@RequestMapping("/complaint")
@Slf4j
public class ComplaintController {

    @Resource
    private ComplaintService complaintService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建投诉
     *
     * @param complaintAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addComplaint(@RequestBody ComplaintAddRequest complaintAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(complaintAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Complaint complaint = new Complaint();
        BeanUtils.copyProperties(complaintAddRequest, complaint);

        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        complaint.setUserId(loginUser.getId());
          List<String> image = complaintAddRequest.getImageList();
          complaint.setImage(JSONUtil.toJsonStr(image));

        // 写入数据库
        boolean result = complaintService.save(complaint);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newComplaintId = complaint.getId();
        return ResultUtils.success(newComplaintId);
    }

    /**
     * 删除投诉
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteComplaint(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Complaint oldComplaint = complaintService.getById(id);
        ThrowUtils.throwIf(oldComplaint == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldComplaint.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = complaintService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新投诉（仅管理员可用）
     *
     * @param complaintUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateComplaint(@RequestBody ComplaintUpdateRequest complaintUpdateRequest) {
        if (complaintUpdateRequest == null || complaintUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Complaint complaint = new Complaint();
        BeanUtils.copyProperties(complaintUpdateRequest, complaint);

        // 判断是否存在
        long id = complaintUpdateRequest.getId();
        Complaint oldComplaint = complaintService.getById(id);
        ThrowUtils.throwIf(oldComplaint == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = complaintService.updateById(complaint);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }


    /**
     * 分页获取投诉列表（封装类）
     *
     * @param complaintQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<ComplaintVO>> listComplaintVOByPage(@RequestBody ComplaintQueryRequest complaintQueryRequest,
                                                               HttpServletRequest request) {
        long current = complaintQueryRequest.getCurrent();
        long size = complaintQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
          User loginUser = userService.getLoginUser(request);
        Page<Complaint> complaintPage = complaintService.page(new Page<>(current, size),
                complaintService.getQueryWrapper(complaintQueryRequest,loginUser));
        // 获取封装类
        return ResultUtils.success(complaintService.getComplaintVOPage(complaintPage, request));
    }


    /**
     * 编辑投诉（给用户使用）
     *
     * @param complaintEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editComplaint(@RequestBody ComplaintEditRequest complaintEditRequest, HttpServletRequest request) {
        if (complaintEditRequest == null || complaintEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Complaint complaint = new Complaint();
        BeanUtils.copyProperties(complaintEditRequest, complaint);
        List<String> image = complaintEditRequest.getImageList();
        if(CollectionUtils.isNotEmpty(image)){
            complaint.setImage(JSONUtil.toJsonStr(image));
        }


        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = complaintEditRequest.getId();
        Complaint oldComplaint = complaintService.getById(id);
        ThrowUtils.throwIf(oldComplaint == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldComplaint.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = complaintService.updateById(complaint);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
