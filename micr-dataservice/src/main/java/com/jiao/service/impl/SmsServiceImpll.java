package com.jiao.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiao.config.JdwsSmsConfig;
import com.jiao.contants.YLBcontants;
import com.jiao.contants.YLBkeys;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import service.SmsService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author 18067
 * @Date 2021/9/17 10:08
 */
@DubboService(interfaceClass = SmsService.class, version = "1.0")
public class SmsServiceImpll implements SmsService {
    @Resource
    private JdwsSmsConfig jdwsSmsConfig;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean handlerSmsService(String phone, String cmd) {
        //生成短信验证码
        String authCode = generateSmsCode(6);
        System.out.println(authCode);
        //调用下发短信
        boolean isSend = sendSms(phone, cmd, authCode);
        if (isSend) {
            String key = YLBkeys.SMS_REG_CODE + phone;
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            ops.set(key, authCode, YLBcontants.SMS_REG_CODE_TIME, TimeUnit.MINUTES);
        }
        return isSend;
    }

    /**
     * 发送短信的方法
     *
     * @param phone 手机号
     * @param cmd   状态是注册,还是登录
     * @return 是否成功
     */
    private boolean sendSms(String phone, String cmd, String authCode) {
        boolean flag = false;
        CloseableHttpClient client = HttpClients.createDefault();
        String url = "";
        if ("regist".equals(cmd)) {
            //注册
            String smsContent = String.format(jdwsSmsConfig.getContent(), authCode);
            url = jdwsSmsConfig.getUrl() + "?mobile=" + phone + "&content=" + smsContent + "&appkey=" + jdwsSmsConfig.getAppkey();
        } else if ("login".equals(cmd)) {
            //登录验证
        }
        HttpGet get = new HttpGet(url);
        try {
            CloseableHttpResponse resp = client.execute(get);
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //String json = EntityUtils.toString(resp.getEntity());
                String json = JSON.toJSONString(resp.getEntity());

                json = "{\n" +
                        "    \"code\": \"10000\",\n" +
                        "    \"charge\": false,\n" +
                        "    \"remain\": 1305,\n" +
                        "    \"msg\": \"查询成功\",\n" +
                        "    \"result\": {\n" +
                        "        \"ReturnStatus\": \"Success\",\n" +
                        "        \"Message\": \"ok\",\n" +
                        "        \"RemainPoint\": 420842,\n" +
                        "        \"TaskID\": 18424321,\n" +
                        "        \"SuccessCounts\": 1\n" +
                        "    }\n" +
                        "}";


                JSONObject jsonObject = JSON.parseObject(json);
                if ("10000".equals(jsonObject.getString("code"))) {
                    JSONObject result = jsonObject.getJSONObject("result");
                    if (result != null) {
                        flag = "Success".equalsIgnoreCase(result.getString("ReturnStatus"));
                    }
                }
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 生成验证码的方法
     *
     * @param len 生成随机数个数
     * @return 返回一个随机数的字符串
     */
    private String generateSmsCode(int len) {
        StringBuilder sb = new StringBuilder();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < len; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 检查验证码是否正确
     *
     * @param phone   手机号
     * @param code验证码
     * @return 布尔型结果
     */
    @Override
    public boolean checkCode(String phone, String code) {
        boolean flag = false;
        String key = YLBkeys.SMS_REG_CODE + phone;
        if (stringRedisTemplate.hasKey(key)) {
            String res = stringRedisTemplate.opsForValue().get(key);
            if (code.equals(res)) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public void deleteKey(String key) {
        stringRedisTemplate.delete(key);
    }


}
