# 数据库初始化
# @author
# @from

-- 创建库
create database if not exists property_management;

-- 切换库
use property_management;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    userName     varchar(256)                           null comment '用户昵称',
    userPhone    varchar(256)                           null comment '用户手机号',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    louDong      varchar(256)                           null comment '楼栋',
    unitNumber   varchar(256)                           null comment '单元号',
    houseNumber  varchar(256)                           null comment '门牌号',
    houseSize    varchar(256)                           null comment '房屋大小',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_userAccount (userAccount)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 缴费项目表
create table if not exists payment_item
(
    id             bigint auto_increment comment 'id' primary key,
    name           varchar(256)                       not null comment '名称',
    amount         varchar(256)                       null comment '金额',
    expirationTime datetime                           null comment '过期时间',
    profile        varchar(1024)                      null comment '简介',
    userId         bigint                             not null comment '创建人',
    createTime     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint  default 0                 not null comment '是否删除'
) comment '缴费项目' collate = utf8mb4_unicode_ci;

-- 缴费记录表
create table if not exists payment_record
(
    id          bigint auto_increment comment 'id' primary key,
    paymentId   bigint                             not null comment '缴费项目id',
    paymentName varchar(256)                       not null comment '项目名称',
    payAmount   varchar(256)                       null comment '缴费金额',
    payDate     datetime                           null comment '缴费时间',
    userId      bigint                             not null comment '创建人',
    userName    varchar(256)                       not null comment '创建人姓名',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
) comment '缴费项目' collate = utf8mb4_unicode_ci;


-- 投诉表
create table if not exists complaint
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(256)                       not null comment '标题',
    content    varchar(1024)                      null comment '详情',
    image      text                               null comment '图片',
    remark     varchar(512)                       null comment '备注',
    status     tinyint  default 0                 not null comment '0-待处理  1-已处理  2-不予处理',
    userId     bigint                             not null comment '创建人',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
) comment '投诉记录' collate = utf8mb4_unicode_ci;

-- 报修表
use property_management;
create table if not exists repairs
(
    id           bigint auto_increment comment 'id' primary key,
    title        varchar(256)                       not null comment '标题',
    content      varchar(1024)                      null comment '详情',
    image        text                               null comment '图片',
    remark       varchar(512)                       null comment '备注',
    status       tinyint  default 0                 not null comment '0-审核中  1-已拒绝  2-维修中  3-无法维修  4-待评价  5-已完成',
    userId       bigint                             not null comment '报修人',
    servicemanId bigint                             null comment '维修人id',
    comment      varchar(512)                       null comment '评价',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除'
) comment '报修记录' collate = utf8mb4_unicode_ci;

-- 访客登记表
use property_management;
create table if not exists visitor
(
    id           bigint auto_increment comment 'id' primary key,
    visitorName  varchar(128)                       not null comment '来访人姓名',
    visitingTime datetime                           null comment '来访时间',
    visitorPhone varchar(128)                       null comment '来访人手机号',
    remark       varchar(512)                       null comment '备注',
    status       tinyint  default 0                 not null comment '0-待审核  1-已通过  2-已拒绝',
    reason       varchar(512)                       null comment '拒绝原因',
    userId       bigint                             not null comment '创建人',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除'
) comment '访客登记' collate = utf8mb4_unicode_ci;


-- 公告表
use property_management;
create table if not exists notice
(
    id          bigint auto_increment comment 'id' primary key,
    title       varchar(128)                       not null comment '标题',
    publishTime datetime                           null comment '发布时间',
    content     text                               null comment '内容',
    status      tinyint  default 0                 not null comment '0-未发布  1-已发布',
    userId      bigint                             not null comment '创建人',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
) comment '公告' collate = utf8mb4_unicode_ci;
