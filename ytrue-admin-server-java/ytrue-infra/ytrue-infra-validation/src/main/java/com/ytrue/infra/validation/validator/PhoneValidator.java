package com.ytrue.infra.validation.validator;

import com.ytrue.infra.validation.annotation.Phone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * 自定义手机号验证器。
 *
 * <p>
 * 实现手机号格式的验证逻辑，支持基本的中国手机号格式。
 * 该验证器用于 {@link Phone} 注解的实现。
 * </p>
 *
 * @author ytrue
 * @date 2024/10/19
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    /**
     * 定义手机号的正则表达式，适用于中国手机号的基本格式。
     */
    private static final String PHONE_NUMBER_REGEX = "^(\\+\\d{1,3}[- ]?)?\\d{10}$";

    /**
     * 初始化方法，用于一些准备工作。
     *
     * @param constraintAnnotation Phone 注解的实例
     */
    @Override
    public void initialize(Phone constraintAnnotation) {
        // 可以在这里进行初始化操作，但本实现不需要进行任何准备工作
    }

    /**
     * 验证手机号是否有效。
     *
     * @param phoneNumber 输入的手机号码
     * @param context 校验上下文
     * @return 如果手机号有效，返回 true；否则返回 false
     */
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        // 如果手机号为空或null，返回false（可以根据业务需求调整）
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false; // 这里可以根据具体需求决定是否允许空值
        }

        // 使用正则表达式进行手机号格式校验
        return Pattern.matches(PHONE_NUMBER_REGEX, phoneNumber);
    }
}
