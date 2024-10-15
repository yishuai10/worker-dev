package com.xiaoqiu.service.impl;

import com.xiaoqiu.pojo.Company;
import com.xiaoqiu.mapper.CompanyMapper;
import com.xiaoqiu.service.CompanyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 企业表 服务实现类
 * </p>
 *
 * @author 小秋
 * @since 2024-10-15
 */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {

}
