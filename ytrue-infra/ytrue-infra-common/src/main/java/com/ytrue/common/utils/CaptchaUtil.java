package com.ytrue.common.utils;

import cn.hutool.core.util.StrUtil;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import com.ytrue.common.enums.CaptchaTypeEnum;

import java.awt.*;

/**
 * @author ytrue
 * @date 2023-06-02 14:59
 * @description 验证码工具类
 */
public class CaptchaUtil {

    /**
     * 验证码内容长度
     */
    private final static int LENGTH = 2;
    /**
     * 验证码宽度
     */
    private final static int WIDTH = 105;
    /**
     * 验证码高度
     */
    private final static int HEIGHT = 36;
    /**
     * 验证码字体
     */
    private final static String FONT_NAME = "";
    /**
     * 字体大小
     */
    private final static int FONT_SIZE = 25;

    /**
     * 获取验证码生产类
     *
     * @param type
     * @return
     */
    public static Captcha getCaptcha(CaptchaTypeEnum type) {
        Captcha captcha;
        switch (type) {
            case ARITHMETIC:
                // 算术类型 https://gitee.com/whvse/EasyCaptcha
                captcha = new FixedArithmeticCaptcha(WIDTH, HEIGHT);
                // 几位数运算，默认是两位
                captcha.setLen(LENGTH);
                break;
            case CHINESE:
                captcha = new ChineseCaptcha(WIDTH, HEIGHT);
                captcha.setLen(LENGTH);
                break;
            case CHINESE_GIF:
                captcha = new ChineseGifCaptcha(WIDTH, HEIGHT);
                captcha.setLen(LENGTH);
                break;
            case GIF:
                captcha = new GifCaptcha(WIDTH, HEIGHT);
                captcha.setLen(LENGTH);
                break;
            case SPEC:
                captcha = new SpecCaptcha(WIDTH, HEIGHT);
                captcha.setLen(LENGTH);
                break;
            default:
                throw new RuntimeException("验证码配置信息错误！正确配置查看 CaptchaTypeEnum ");
        }
        if (StrUtil.isNotBlank(FONT_NAME)) {
            captcha.setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE));
        }
        return captcha;
    }

    private static class FixedArithmeticCaptcha extends ArithmeticCaptcha {
        public FixedArithmeticCaptcha(int width, int height) {
            super(width, height);
        }

        @Override
        protected char[] alphas() {
            // 生成随机数字和运算符
            int n1 = num(1, 10), n2 = num(1, 10);
            int opt = num(3);

            // 计算结果
            int res = new int[]{n1 + n2, n1 - n2, n1 * n2}[opt];
            // 转换为字符运算符
            char optChar = "+-x".charAt(opt);

            this.setArithmeticString(String.format("%s%c%s=?", n1, optChar, n2));
            this.chars = String.valueOf(res);

            return chars.toCharArray();
        }
    }

}
