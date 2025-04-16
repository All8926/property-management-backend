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
    status     tinyint  default 0                 not null comment '0-待处理  1-不予处理  2-已处理',
    userId     bigint                             not null comment '创建人',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
) comment '投诉记录' collate = utf8mb4_unicode_ci;


-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏';
