package com.ytrue.controller.common;

import cn.hutool.core.util.IdUtil;
import com.wf.captcha.base.Captcha;
import com.ytrue.infra.cache.constant.CacheKey;
import com.ytrue.infra.core.constant.StrPool;
import com.ytrue.infra.core.enums.CaptchaTypeEnum;
import com.ytrue.infra.core.response.ServerResponseEntity;
import com.ytrue.infra.core.util.CaptchaUtil;
import com.ytrue.tools.security.annotation.IgnoreWebSecurity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@Tag(name = "验证码")
@RestController
@RequiredArgsConstructor
public class CaptchaController {

    private final StringRedisTemplate redisTemplate;

    @IgnoreWebSecurity
    @Operation(summary = "获取验证码")
    @GetMapping("/captcha")
    public ServerResponseEntity<Map<String, Object>> captcha() {
        Captcha captcha = CaptchaUtil.getCaptcha(CaptchaTypeEnum.ARITHMETIC);
        // 设置缓存的key
        String uuid = IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == CaptchaTypeEnum.ARITHMETIC.ordinal() && captchaValue.contains(StrPool.DOT)) {
            captchaValue = captchaValue.split("\\.")[0];
        }

        // 缓存,2分钟过期
        redisTemplate.opsForValue().set(CacheKey.ADMIN_LOGIN_CAPTCHA + uuid, captchaValue, 2L, TimeUnit.MINUTES);

        // 验证码信息
        Map<String, Object> result = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return ServerResponseEntity.success(result);
    }

}
