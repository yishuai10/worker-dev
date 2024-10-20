package com.xiaoqiu.service.impl;

import com.xiaoqiu.pojo.Article;
import com.xiaoqiu.mapper.ArticleMapper;
import com.xiaoqiu.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author 小秋
 * @since 2024-10-20
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

}
