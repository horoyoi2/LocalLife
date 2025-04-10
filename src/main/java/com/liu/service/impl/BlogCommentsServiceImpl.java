package com.liu.service.impl;

import com.liu.entity.BlogComments;
import com.liu.mapper.BlogCommentsMapper;
import com.liu.service.IBlogCommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComments> implements IBlogCommentsService {

}
