package com.liu.service;

import com.liu.dto.Result;
import com.liu.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IBlogService extends IService<Blog> {
    Result queryHotBlog(Integer current);
    Result queryBlogById(Long id);
    Result likeBlog(Long id);
    Result saveBlog(Blog blog);
    Result queryBlogLikes(Long id);
    Result queryBlogOfFollow(Long max,Integer offset);



}
