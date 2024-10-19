package com.ytrue.infra.core.util;

import cn.hutool.core.util.StrUtil;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;

import java.awt.*;

/**
 * 验证码工具类，提供生成不同类型验证码的功能。
 *
 * @author ytrue
 * @date 2023-06-02 14:59
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
     * 获取指定类型的验证码实例。
     *
     * @param type 验证码类型枚举
     * @return 生成的验证码实例
     */
    public static Captcha getCaptcha(CaptchaTypeEnum type) {
        Captcha captcha;
        switch (type) {
            case ARITHMETIC -> {
                // 算术类型验证码
                captcha = new FixedArithmeticCaptcha(WIDTH, HEIGHT);
                captcha.setLen(LENGTH);  // 设置算式的长度
            }
            case CHINESE -> {
                // 中文类型验证码
                captcha = new ChineseCaptcha(WIDTH, HEIGHT);
                captcha.setLen(LENGTH);  // 设置字符长度
            }
            case CHINESE_GIF -> {
                // 中文 GIF 类型验证码
                captcha = new ChineseGifCaptcha(WIDTH, HEIGHT);
                captcha.setLen(LENGTH);  // 设置字符长度
            }
            case GIF -> {
                // GIF 类型验证码
                captcha = new GifCaptcha(WIDTH, HEIGHT);
                captcha.setLen(LENGTH);  // 设置字符长度
            }
            case SPEC -> {
                // 特殊类型验证码
                captcha = new SpecCaptcha(WIDTH, HEIGHT);
                captcha.setLen(LENGTH);  // 设置字符长度
            }
            default -> throw new RuntimeException("验证码配置信息错误！正确配置查看 CaptchaTypeEnum ");
        }

        // 如果字体名称不为空，设置字体
        if (StrUtil.isNotBlank(FONT_NAME)) {
            captcha.setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE));
        }
        return captcha;  // 返回生成的验证码
    }

    /**
     * 固定算术验证码类，生成固定格式的算术题验证码。
     */
    private static class FixedArithmeticCaptcha extends ArithmeticCaptcha {
        public FixedArithmeticCaptcha(int width, int height) {
            super(width, height);
        }

        @Override
        protected char[] alphas() {
            // 生成随机数字和运算符
            int n1 = num(1, 10), n2 = num(1, 10);
            int opt = num(3);  // 随机选择运算符

            // 计算结果
            int res = new int[]{n1 + n2, n1 - n2, n1 * n2}[opt];
            // 转换为字符运算符
            char optChar = "+-x".charAt(opt);

            // 设置算术字符串
            this.setArithmeticString(String.format("%s%c%s=?", n1, optChar, n2));
            this.chars = String.valueOf(res);  // 设置结果为字符

            return chars.toCharArray();  // 返回结果字符数组
        }
    }

    /**
     * 验证码类型枚举，定义支持的验证码类型。
     */
    public enum CaptchaTypeEnum {
        /**
         * 算术类型
         */
        ARITHMETIC,

        /**
         * 中文类型
         */
        CHINESE,

        /**
         * 中文 GIF 类型
         */
        CHINESE_GIF,

        /**
         * GIF 类型
         */
        GIF,

        /**
         * 特殊类型
         */
        SPEC
    }
}
