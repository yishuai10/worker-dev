package com.xiaoqiu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoqiu.mapper.ResumeMapper;
import com.xiaoqiu.pojo.Resume;
import com.xiaoqiu.service.IResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 简历表 服务实现类
 * </p>
 *
 * @author 小秋
 * @since 2024-10-29
 */
@Service
public class ResumeServiceImpl extends ServiceImpl<ResumeMapper, Resume> implements IResumeService {

    @Autowired
    private ResumeMapper resumeMapper;


    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void initResume(String userId) {
        // 模拟除0异常
//        int a = 1/0;
        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setCreateTime(LocalDateTime.now());
        resume.setUpdatedTime(LocalDateTime.now());
        resumeMapper.insert(resume);
    }

//    @Transactional
//    @Override
//    public void initResume(String userId, String msgId) {
//        Resume resume = new Resume();
//        resume.setUserId(userId);
//        resume.setCreateTime(LocalDateTime.now());
//        resume.setUpdatedTime(LocalDateTime.now());
//
//        resumeMapper.insert(resume);
//
//
//        // 最终一致性测试：resumeId最后以为如果是单数，则抛出异常；如果是偶数则正常入库
//        String resumeId = resume.getId();
//        String tail = resumeId.substring(resumeId.length() - 1);
//        Integer tailNum = Integer.valueOf(tail);
//        if (tailNum % 2 == 0) {
//            log.info("简历初始化成功...");
//
//            // 删除对应的本地消息表记录
//            log.info("本地消息删除，tailNum为{}，该消息ID为{}", tailNum, msgId);
//            recordService.removeById(msgId);
//        } else {
//            log.info("简历初始化失败... tailNum为{}，该消息ID为{}", tailNum, msgId);
//            throw new RuntimeException("简历初始化失败...");
//        }


}