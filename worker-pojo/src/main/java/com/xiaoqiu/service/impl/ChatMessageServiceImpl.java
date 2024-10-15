package com.xiaoqiu.service.impl;

import com.xiaoqiu.pojo.ChatMessage;
import com.xiaoqiu.mapper.ChatMessageMapper;
import com.xiaoqiu.service.ChatMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 聊天信息存储表 服务实现类
 * </p>
 *
 * @author 小秋
 * @since 2024-10-15
 */
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {

}
