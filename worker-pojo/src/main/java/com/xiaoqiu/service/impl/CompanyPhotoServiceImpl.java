package com.xiaoqiu.service.impl;

import com.xiaoqiu.pojo.CompanyPhoto;
import com.xiaoqiu.mapper.CompanyPhotoMapper;
import com.xiaoqiu.service.CompanyPhotoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 企业相册表，本表只存企业上传的图片 服务实现类
 * </p>
 *
 * @author 小秋
 * @since 2024-10-15
 */
@Service
public class CompanyPhotoServiceImpl extends ServiceImpl<CompanyPhotoMapper, CompanyPhoto> implements CompanyPhotoService {

}
