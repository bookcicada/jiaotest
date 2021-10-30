package com.jiao.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiao.config.JdwxRealNameConfig;
import com.jiao.util.HttpClientUtils;
import service.RealNameService;
import service.UserService;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author 18067
 * @Date 2021/9/19 19:22
 */
public class RealNameServiceImpl implements RealNameService {
    @Resource
    JdwxRealNameConfig realNameConfig;

    @Resource
    UserService userService;

    /**
     * 处理实名认证
     * @param idCard 身份证
     * @param name姓名
     * @param phone手机号
     * @return 更新结果
     */
    public boolean handerRealName(String idCard, String name,String phone) {
        boolean result=false;
        boolean isOk = invokeRealNameAPI(idCard, name);
        if (isOk) {
            //说明认证通过
            result=userService.realName(phone,name,idCard);
        }
        return result;
    }


    /**
     * 调用第三方接口确认身份证姓名是否正确
     * @param idcard 身份证号
     * @param name   姓名
     * @return 认证结果
     */
    private boolean invokeRealNameAPI(String idcard, String name) {
        boolean flag = false;
        HashMap param = new HashMap();
        param.put("cardNo", idcard);
        param.put("realName", name);
        param.put("appkey", realNameConfig.getAppkey());
        try {
            String res = HttpClientUtils.doGet(realNameConfig.getUrl(), param);
            res = "{\n" +
                    "    \"code\": \"10000\",\n" +
                    "    \"charge\": false,\n" +
                    "    \"remain\": 1305,\n" +
                    "    \"msg\": \"查询成功\",\n" +
                    "    \"result\": {\n" +
                    "        \"error_code\": 0,\n" +
                    "        \"reason\": \"成功\",\n" +
                    "        \"result\": {\n" +
                    "            \"realname\": \""+name+"\",\n" +
                    "            \"idcard\": \""+idcard+"\",\n" +
                    "            \"isok\": true\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            if (res != null || res != "") {
                JSONObject jsonObject = JSON.parseObject(res);
                if ("10000".equals(jsonObject.getString("code"))) {
                    flag = jsonObject.getJSONObject("result").getJSONObject("result").getBoolean("isok");
                }
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }
}
