package com.xiaoqiu.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoqiu.pojo.Admin;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 慕聘网运营管理系统的admin账户表，仅登录，不提供注册 Mapper 接口
 * </p>
 *
 * @author xiaoqiu
 * @since 2022-09-04
 */
@Repository
public interface AdminMapper extends BaseMapper<Admin> {

}
