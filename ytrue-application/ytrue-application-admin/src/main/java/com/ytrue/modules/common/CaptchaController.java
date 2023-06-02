package com.ytrue.modules.common;

import cn.hutool.core.util.IdUtil;
import com.wf.captcha.base.Captcha;
import com.ytrue.common.constant.RedisKeyPrefix;
import com.ytrue.common.constant.StrPool;
import com.ytrue.common.enums.CaptchaTypeEnum;
import com.ytrue.common.utils.ApiResultResponse;
import com.ytrue.common.utils.CaptchaUtil;
import com.ytrue.tools.security.annotation.IgnoreWebSecurity;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ytrue
 * @date 2023-06-02 14:32
 * @description CaptchaController
 */
@RestController
@AllArgsConstructor
public class CaptchaController {

    private final StringRedisTemplate redisTemplate;

    @IgnoreWebSecurity
    @GetMapping("/captcha")
    public ApiResultResponse<Map<String, Object>> captcha() {

        Captcha captcha = CaptchaUtil.getCaptcha(CaptchaTypeEnum.ARITHMETIC);
        // 设置缓存的key
        String uuid = IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == CaptchaTypeEnum.ARITHMETIC.ordinal() && captchaValue.contains(StrPool.DOT)) {
            captchaValue = captchaValue.split("\\.")[0];
        }

        // 缓存,2分钟过期
        redisTemplate.opsForValue().set(RedisKeyPrefix.ADMIN_LOGIN_CAPTCHA + uuid, captchaValue, 2L, TimeUnit.MINUTES);

        // 验证码信息
        Map<String, Object> result = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return ApiResultResponse.success(result);
    }

}
