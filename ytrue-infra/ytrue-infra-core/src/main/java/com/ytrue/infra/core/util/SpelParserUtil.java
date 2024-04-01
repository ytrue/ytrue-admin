package com.ytrue.infra.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author ytrue
 * @date 2023-11-28 16:21
 * @description SpelParserUtil
 */
@Slf4j
public final class SpelParserUtil {

    private static final String EXPRESSION_PREFIX = "#{";

    private static final String EXPRESSION_SUFFIX = "}";

    /**
     * 表达式解析器
     */
    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    /**
     * 参数名解析器，用于获取参数名
     */
    private static final DefaultParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();


    private SpelParserUtil() {
    }

    /**
     * 解析spel表达式
     *
     * @param method         方法
     * @param args           参数值
     * @param spelExpression 表达式
     * @param clz            返回结果的类型
     * @param defaultResult  默认结果
     * @return 执行spel表达式后的结果
     */
    public static <T> T parse(Method method, Object[] args, String spelExpression, Class<T> clz, T defaultResult) {
        String[] params = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        //设置上下文变量
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);
        }
        T result = getResult(context, spelExpression, clz);
        if (Objects.isNull(result)) {
            return defaultResult;
        }
        return result;
    }

    /**
     * 解析spel表达式
     *
     * @param method         方法
     * @param args           参数值
     * @param spelExpression 表达式
     * @param clz            返回结果的类型
     * @return 执行spel表达式后的结果
     */
    public static <T> T parse(Method method, Object[] args, String spelExpression, Class<T> clz) {
        String[] params = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        //设置上下文变量
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);
        }
        return getResult(context, spelExpression, clz);
    }

    /**
     * 解析spel表达式
     *
     * @param param          参数名
     * @param paramValue     参数值
     * @param spelExpression 表达式
     * @param clz            返回结果的类型
     * @return 执行spel表达式后的结果
     */
    public static <T> T parse(String param, Object paramValue, String spelExpression, Class<T> clz) {
        EvaluationContext context = new StandardEvaluationContext();
        //设置上下文变量
        context.setVariable(param, paramValue);
        return getResult(context, spelExpression, clz);
    }


    /**
     * 解析spel表达式
     *
     * @param param          参数名
     * @param paramValue     参数值
     * @param spelExpression 表达式
     * @param clz            返回结果的类型
     * @param defaultResult  默认结果
     * @return 执行spel表达式后的结果
     */
    public static <T> T parse(String param, Object paramValue, String spelExpression, Class<T> clz, T defaultResult) {
        EvaluationContext context = new StandardEvaluationContext();
        //设置上下文变量
        context.setVariable(param, paramValue);
        T result = getResult(context, spelExpression, clz);
        if (Objects.isNull(result)) {
            return defaultResult;
        }
        return result;

    }


    /**
     * 获取spel表达式后的结果
     *
     * @param context        解析器上下文接口
     * @param spelExpression 表达式
     * @param clz            返回结果的类型
     * @return 执行spel表达式后的结果
     */
    private static <T> T getResult(EvaluationContext context, String spelExpression, Class<T> clz) {
        try {
            //解析表达式
            Expression expression = parseExpression(spelExpression);
            //获取表达式的值
            return expression.getValue(context, clz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * 解析表达式
     *
     * @param spelExpression spel表达式
     * @return
     */
    private static Expression parseExpression(String spelExpression) {
        // 如果表达式是一个#{}表达式，需要为解析传入模板解析器上下文
        if (spelExpression.startsWith(EXPRESSION_PREFIX) && spelExpression.endsWith(EXPRESSION_SUFFIX)) {
            return EXPRESSION_PARSER.parseExpression(spelExpression, new TemplateParserContext());
        }

        return EXPRESSION_PARSER.parseExpression(spelExpression);
    }




}
