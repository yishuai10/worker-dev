package com.xiaoqiu.service.impl;

import com.xiaoqiu.pojo.Orders;
import com.xiaoqiu.mapper.OrdersMapper;
import com.xiaoqiu.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author 小秋
 * @since 2024-10-20
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

}
