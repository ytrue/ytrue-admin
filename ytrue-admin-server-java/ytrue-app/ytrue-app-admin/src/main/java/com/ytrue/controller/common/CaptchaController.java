package com.ytrue.controller.common;

import cn.hutool.core.util.IdUtil;
import com.wf.captcha.base.Captcha;
import com.ytrue.bean.resp.common.CaptchaResp;
import com.ytrue.infra.core.constant.CacheKey;
import com.ytrue.infra.core.constant.StrPool;
import com.ytrue.infra.core.response.ServerResponseEntity;
import com.ytrue.infra.core.util.CaptchaUtil;
import com.ytrue.infra.security.annotation.IgnoreWebSecurity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ServerResponseEntity<CaptchaResp> captcha() {
        Captcha captcha = CaptchaUtil.getCaptcha(CaptchaUtil.CaptchaTypeEnum.ARITHMETIC);
        // 设置缓存的key
        String uuid = IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == CaptchaUtil.CaptchaTypeEnum.ARITHMETIC.ordinal() && captchaValue.contains(StrPool.DOT)) {
            captchaValue = captchaValue.split("\\.")[0];
        }

        // 缓存,2分钟过期
        redisTemplate.opsForValue().set(CacheKey.ADMIN_LOGIN_CAPTCHA + uuid, captchaValue, 2L, TimeUnit.MINUTES);

        // 验证码信息
        CaptchaResp captchaResp = new CaptchaResp();
        captchaResp.setImg(captcha.toBase64());
        captchaResp.setUuid(uuid);
        return ServerResponseEntity.success(captchaResp);
    }

}
