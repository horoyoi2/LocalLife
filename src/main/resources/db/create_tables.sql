create table tb_blog
(
    id          bigint unsigned auto_increment comment '主键'
        primary key,
    shop_id     bigint                                   not null comment '商户id',
    user_id     bigint unsigned                          not null comment '用户id',
    title       varchar(255) collate utf8mb4_unicode_ci  not null comment '标题',
    images      varchar(2048)                            not null comment '探店的照片，最多9张，多张以","隔开',
    content     varchar(2048) collate utf8mb4_unicode_ci not null comment '探店的文字描述',
    liked       int unsigned default '0'                 null comment '点赞数量',
    comments    int unsigned                             null comment '评论数量',
    create_time timestamp    default CURRENT_TIMESTAMP   not null comment '创建时间',
    update_time timestamp    default CURRENT_TIMESTAMP   not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_general_ci
    row_format = COMPACT;

create table tb_blog_comments
(
    id          bigint unsigned auto_increment comment '主键'
        primary key,
    user_id     bigint unsigned                     not null comment '用户id',
    blog_id     bigint unsigned                     not null comment '探店id',
    parent_id   bigint unsigned                     not null comment '关联的1级评论id，如果是一级评论，则值为0',
    answer_id   bigint unsigned                     not null comment '回复的评论id',
    content     varchar(255)                        not null comment '回复的内容',
    liked       int unsigned                        null comment '点赞数',
    status      tinyint unsigned                    null comment '状态，0：正常，1：被举报，2：禁止查看',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_general_ci
    row_format = COMPACT;

create table tb_follow
(
    id             bigint auto_increment comment '主键'
        primary key,
    user_id        bigint unsigned                     not null comment '用户id',
    follow_user_id bigint unsigned                     not null comment '关联的用户id',
    create_time    timestamp default CURRENT_TIMESTAMP not null comment '创建时间'
)
    collate = utf8mb4_general_ci
    row_format = COMPACT;

create table tb_seckill_voucher
(
    voucher_id  bigint unsigned                     not null comment '关联的优惠券的id'
        primary key,
    stock       int                                 not null comment '库存',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    begin_time  timestamp default CURRENT_TIMESTAMP not null comment '生效时间',
    end_time    timestamp default CURRENT_TIMESTAMP not null comment '失效时间',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '秒杀优惠券表，与优惠券是一对一关系' collate = utf8mb4_general_ci
                                                row_format = COMPACT;

create table tb_shop
(
    id          bigint unsigned auto_increment comment '主键'
        primary key,
    name        varchar(128)                        not null comment '商铺名称',
    type_id     bigint unsigned                     not null comment '商铺类型的id',
    images      varchar(1024)                       not null comment '商铺图片，多个图片以'',''隔开',
    area        varchar(128)                        null comment '商圈，例如陆家嘴',
    address     varchar(255)                        not null comment '地址',
    x           double unsigned                     not null comment '经度',
    y           double unsigned                     not null comment '维度',
    avg_price   bigint unsigned                     null comment '均价，取整数',
    sold        int unsigned zerofill               not null comment '销量',
    comments    int unsigned zerofill               not null comment '评论数量',
    score       int(2) unsigned zerofill            not null comment '评分，1~5分，乘10保存，避免小数',
    open_hours  varchar(32)                         null comment '营业时间，例如 10:00-22:00',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_general_ci
    row_format = COMPACT;

create index foreign_key_type
    on tb_shop (type_id);

create table tb_shop_type
(
    id          bigint unsigned auto_increment comment '主键'
        primary key,
    name        varchar(32)                         null comment '类型名称',
    icon        varchar(255)                        null comment '图标',
    sort        int unsigned                        null comment '顺序',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_general_ci
    row_format = COMPACT;

create table tb_sign
(
    id        bigint unsigned auto_increment comment '主键'
        primary key,
    user_id   bigint unsigned  not null comment '用户id',
    year      year             not null comment '签到的年',
    month     tinyint          not null comment '签到的月',
    date      date             not null comment '签到的日期',
    is_backup tinyint unsigned null comment '是否补签'
)
    collate = utf8mb4_general_ci
    row_format = COMPACT;

create table tb_user
(
    id          bigint unsigned auto_increment comment '主键'
        primary key,
    phone       varchar(11)                            not null comment '手机号码',
    password    varchar(128) default ''                null comment '密码，加密存储',
    nick_name   varchar(32)  default ''                null comment '昵称，默认是用户id',
    icon        varchar(255) default ''                null comment '人物头像',
    create_time timestamp    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uniqe_key_phone
        unique (phone)
)
    collate = utf8mb4_general_ci
    row_format = COMPACT;

create table tb_user_info
(
    user_id     bigint unsigned                            not null comment '主键，用户id'
        primary key,
    city        varchar(64)      default ''                null comment '城市名称',
    introduce   varchar(128)                               null comment '个人介绍，不要超过128个字符',
    fans        int unsigned     default '0'               null comment '粉丝数量',
    followee    int unsigned     default '0'               null comment '关注的人的数量',
    gender      tinyint unsigned default '0'               null comment '性别，0：男，1：女',
    birthday    date                                       null comment '生日',
    credits     int unsigned     default '0'               null comment '积分',
    level       tinyint unsigned default '0'               null comment '会员级别，0~9级,0代表未开通会员',
    create_time timestamp        default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp        default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_general_ci
    row_format = COMPACT;

create table tb_voucher
(
    id           bigint unsigned auto_increment comment '主键'
        primary key,
    shop_id      bigint unsigned                            null comment '商铺id',
    title        varchar(255)                               not null comment '代金券标题',
    sub_title    varchar(255)                               null comment '副标题',
    rules        varchar(1024)                              null comment '使用规则',
    pay_value    bigint unsigned                            not null comment '支付金额，单位是分。例如200代表2元',
    actual_value bigint                                     not null comment '抵扣金额，单位是分。例如200代表2元',
    type         tinyint unsigned default '0'               not null comment '0,普通券；1,秒杀券',
    status       tinyint unsigned default '1'               not null comment '1,上架; 2,下架; 3,过期',
    create_time  timestamp        default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  timestamp        default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_general_ci
    row_format = COMPACT;

create table tb_voucher_order
(
    id          bigint                                     not null comment '主键'
        primary key,
    user_id     bigint unsigned                            not null comment '下单的用户id',
    voucher_id  bigint unsigned                            not null comment '购买的代金券id',
    pay_type    tinyint unsigned default '1'               not null comment '支付方式 1：余额支付；2：支付宝；3：微信',
    status      tinyint unsigned default '1'               not null comment '订单状态，1：未支付；2：已支付；3：已核销；4：已取消；5：退款中；6：已退款',
    create_time timestamp        default CURRENT_TIMESTAMP not null comment '下单时间',
    pay_time    timestamp                                  null comment '支付时间',
    use_time    timestamp                                  null comment '核销时间',
    refund_time timestamp                                  null comment '退款时间',
    update_time timestamp        default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_general_ci
    row_format = COMPACT;


