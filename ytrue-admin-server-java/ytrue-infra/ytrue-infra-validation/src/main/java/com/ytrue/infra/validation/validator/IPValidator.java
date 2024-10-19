package com.ytrue.infra.validation.validator;

import com.ytrue.infra.validation.annotation.IP;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.InetAddress;

/**
 * 自定义IP地址验证器。
 *
 * <p>
 * 实现IP地址格式的验证逻辑。该验证器用于 {@link IP} 注解的实现。
 * </p>
 *
 * @author ytrue
 * @date 2024/10/19
 */
public class IPValidator implements ConstraintValidator<IP, String> {

    /**
     * 校验方法，用于验证输入的IP地址是否有效。
     *
     * @param ip 输入的IP地址
     * @param context 校验上下文
     * @return 如果IP地址有效，返回 true；否则返回 false
     */
    @Override
    public boolean isValid(String ip, ConstraintValidatorContext context) {
        try {
            // 尝试解析IP地址，如果成功则认为有效
            InetAddress.getByName(ip);
            return true;
        } catch (Exception e) {
            // 解析失败则认为无效
            return false;
        }
    }
}
