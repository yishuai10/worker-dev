package com.xiaoqiu.utils;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.xiaoqiu.exception.XiaoQiuException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.xiaoqiu.resp.ResponseStatusEnum;

/**
 * @author xiaoqiu
 */
@Component
@Slf4j
public class SMSUtils {

    @Value("${sms.secretId:test}")
    private String secretId;
    @Value("${sms.secretKey:test}")
    private String secretKey;
    @Value("${sms.templateId:test}")
    private String templateId;
    @Value("${sms.sign.name:小秋}")
    private String signName;
    @Value("${sms.sdkAppId: 10000}")
    private String sdkAppId;
    @Value("${sms.test.switch:true}")
    private boolean smsTestSwitch;

    public void sendSMS(String phone, String code) {
        try {
            // 测试避免频发发送短信，关闭认证，直接成功
            if (smsTestSwitch) {
                log.info("测试环境，短信发送成功！手机号：{}， 验证码：{}", phone, code);
                return;
            }

            if (StringUtils.isBlank(phone) || StringUtils.isBlank(code)) {
                throw new XiaoQiuException(ResponseStatusEnum.MOBILE_ERROR);
            }

            // 必要步骤：CAM密匙查询获取: https://console.cloud.tencent.com/cam/capi
            SmsClient client = getSmsClient();

            // 实例化一个请求对象,每个接口都会对应一个request对象
            SendSmsRequest req = getSendSmsRequest(phone, code);

            // 返回的resp是一个SendSmsResponse的实例，与请求对象对应
            SendSmsResponse resp = client.SendSms(req);
            // 输出json格式的字符串回包
            log.info("手机号：{}, code: {}, 短信发送结果：{}", phone, code, SendSmsResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            log.error("发送短信失败！", e);
            throw new XiaoQiuException(ResponseStatusEnum.MOBILE_ERROR);
        }
    }

    private SmsClient getSmsClient() {
        Credential cred = new Credential(secretId, secretKey);

        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
//            httpProfile.setReqMethod("POST"); // 默认使用POST

        /* SDK会自动指定域名。通常是不需要特地指定域名的，但是如果你访问的是金融区的服务
         * 则必须手动指定域名，例如sms的上海金融区域名： sms.ap-shanghai-fsi.tencentcloudapi.com */
        httpProfile.setEndpoint("sms.tencentcloudapi.com");

        // 实例化一个client选项
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        return new SmsClient(cred, "ap-nanjing", clientProfile);
    }

    private SendSmsRequest getSendSmsRequest(String phone, String code) {
        SendSmsRequest req = new SendSmsRequest();
        String[] phoneNumberSet = {"+86" + phone};//电话号码
        req.setPhoneNumberSet(phoneNumberSet);
        // 短信应用ID: 短信SdkAppId在 [短信控制台] 添加应用后生成的实际SdkAppId
        req.setSmsSdkAppId(sdkAppId);
        // 签名
        req.setSignName(signName);
        // 模板id：必须填写已审核通过的模板 ID。模板ID可登录 [短信控制台] 查看
        req.setTemplateId(templateId);

        /* 模板参数（自定义占位变量）: 若无模板参数，则设置为空 */
        String[] templateParamSet1 = {code};
        req.setTemplateParamSet(templateParamSet1);
        return req;
    }

//    可以启动一个main函数测试
//    public static void main(String[] args) {
//        try {
//            new SMSUtils().sendSMS("18812348888", "8888");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}