package com.app.project.controller;

import com.app.project.annotation.AuthCheck;
import com.app.project.common.BaseResponse;
import com.app.project.common.DeleteRequest;
import com.app.project.common.ErrorCode;
import com.app.project.common.ResultUtils;
import com.app.project.constant.UserConstant;
import com.app.project.exception.BusinessException;
import com.app.project.exception.ThrowUtils;
import com.app.project.model.dto.notice.NoticeAddRequest;
import com.app.project.model.dto.notice.NoticeQueryRequest;
import com.app.project.model.dto.notice.NoticeUpdateRequest;
import com.app.project.model.entity.Notice;
import com.app.project.model.entity.User;
import com.app.project.model.vo.NoticeVO;
import com.app.project.service.NoticeService;
import com.app.project.service.UserService;
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
 * 公告接口
 *
 * @author
 * @from
 */
@RestController
@RequestMapping("/notice")
@Slf4j
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建公告
     *
     * @param noticeAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addNotice(@Valid @RequestBody NoticeAddRequest noticeAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(noticeAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeAddRequest, notice);

        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        notice.setUserId(loginUser.getId());

        // 写入数据库
        boolean result = noticeService.save(notice);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newNoticeId = notice.getId();
        return ResultUtils.success(newNoticeId);
    }

    /**
     * 删除公告
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteNotice(@Valid @RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        // 判断是否存在
        Notice oldNotice = noticeService.getById(id);
        ThrowUtils.throwIf(oldNotice == null, ErrorCode.NOT_FOUND_ERROR);

        // 操作数据库
        boolean result = noticeService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新公告（仅管理员可用）
     *
     * @param noticeUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateNotice(@Valid @RequestBody NoticeUpdateRequest noticeUpdateRequest) {
        if (noticeUpdateRequest == null || noticeUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeUpdateRequest, notice);

        // 判断是否存在
        long id = noticeUpdateRequest.getId();
        Notice oldNotice = noticeService.getById(id);
        ThrowUtils.throwIf(oldNotice == null, ErrorCode.NOT_FOUND_ERROR);

        // 操作数据库
        boolean result = noticeService.updateById(notice);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }


    /**
     * 分页获取公告列表（封装类）
     *
     * @param noticeQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<NoticeVO>> listNoticeVOByPage( @RequestBody NoticeQueryRequest noticeQueryRequest,
                                                               HttpServletRequest request) {
        long current = noticeQueryRequest.getCurrent();
        long size = noticeQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
          User loginUser = userService.getLoginUser(request);
        Page<Notice> noticePage = noticeService.page(new Page<>(current, size),
                noticeService.getQueryWrapper(noticeQueryRequest,loginUser));
        // 获取封装类
        return ResultUtils.success(noticeService.getNoticeVOPage(noticePage, request));
    }
 

    // endregion
}
