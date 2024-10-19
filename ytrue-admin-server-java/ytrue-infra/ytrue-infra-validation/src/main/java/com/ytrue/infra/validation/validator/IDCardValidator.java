package com.ytrue.infra.validation.validator;

import com.ytrue.infra.validation.annotation.IDCard;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * 自定义身份证号码验证器。
 *
 * <p>
 * 实现身份证号码格式的验证逻辑，支持15位和18位身份证号码的校验。
 * 该验证器用于 {@link IDCard} 注解的实现。
 * </p>
 *
 * @author ytrue
 * @date 2024/10/19
 */
public class IDCardValidator implements ConstraintValidator<IDCard, String> {

    // 身份证号码的正则表达式（支持15位和18位）
    private static final String ID_CARD_REGEX = "^(\\d{15}|\\d{17}[0-9Xx])$";

    /**
     * 初始化方法，用于一些准备工作。
     * 在注解被应用时调用，但本实现不需要进行任何初始化操作。
     *
     * @param constraintAnnotation IDCard 注解的实例
     */
    @Override
    public void initialize(IDCard constraintAnnotation) {
        // 初始化方法，可以用于一些准备工作
    }

    /**
     * 校验方法，用于验证输入的身份证号码是否有效。
     *
     * @param idCard 输入的身份证号码
     * @param context 校验上下文
     * @return 如果身份证号码有效，返回 true；否则返回 false
     */
    @Override
    public boolean isValid(String idCard, ConstraintValidatorContext context) {
        // 如果身份证号码为空或null，返回false（可以根据业务需求调整）
        if (idCard == null || idCard.isEmpty()) {
            return false; // 这里可以根据具体需求决定是否允许空值
        }

        // 使用正则表达式进行身份证号码格式校验
        return Pattern.matches(ID_CARD_REGEX, idCard);
    }
}
