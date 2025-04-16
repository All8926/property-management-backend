package com.app.project.service.impl;

import com.app.project.mapper.ComplaintMapper;
import com.app.project.model.entity.Complaint;
import com.app.project.service.ComplaintService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【complaint(投诉记录)】的数据库操作Service实现
* @createDate 2025-04-16 21:44:19
*/
@Service
public class ComplaintServiceImpl extends ServiceImpl<ComplaintMapper, Complaint>
    implements ComplaintService {

}




