package com.ytrue.tools.document;

import com.github.xiaoymin.knife4j.spring.filter.ProductionSecurityFilter;
import com.github.xiaoymin.knife4j.spring.filter.SecurityBasicAuthFilter;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ytrue
 * @date 2022/4/22 10:14
 * @description Swagger自动配置类
 */
@ConditionalOnWebApplication
@EnableSwagger2WebMvc
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(name = "ytrue.document.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class SwaggerAutoConfiguration implements BeanFactoryAware {

    private final SwaggerProperties swaggerProperties;

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }


    /**
     * 创建多组
     *
     * @return {@link  List<Docket>}
     */
    @Bean
    @ConditionalOnMissingBean
    public List<Docket> createRestApi() {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        List<Docket> docketList = new LinkedList<>();

        // 没有分组
        if (swaggerProperties.getDocket().isEmpty()) {
            Docket docket = createDocket(swaggerProperties);
            configurableBeanFactory.registerSingleton(swaggerProperties.getTitle(), docket);
            docketList.add(docket);
            return docketList;
        }

        // 分组创建
        for (String groupName : swaggerProperties.getDocket().keySet()) {
            SwaggerProperties.DocketInfo docketInfo = swaggerProperties.getDocket().get(groupName);

            //API 基础信息
            ApiInfo apiInfo = new ApiInfoBuilder()
                    .title(docketInfo.getTitle().isEmpty() ? swaggerProperties.getTitle() : docketInfo.getTitle())
                    .description(docketInfo.getDescription().isEmpty() ? swaggerProperties.getDescription() : docketInfo.getDescription())
                    .version(docketInfo.getVersion().isEmpty() ? swaggerProperties.getVersion() : docketInfo.getVersion())
                    .license(docketInfo.getLicense().isEmpty() ? swaggerProperties.getLicense() : docketInfo.getLicense())
                    .licenseUrl(docketInfo.getLicenseUrl().isEmpty() ? swaggerProperties.getLicenseUrl() : docketInfo.getLicenseUrl())
                    .contact(
                            new Contact(
                                    docketInfo.getContact().getName().isEmpty() ? swaggerProperties.getContact().getName() : docketInfo.getContact().getName(),
                                    docketInfo.getContact().getUrl().isEmpty() ? swaggerProperties.getContact().getUrl() : docketInfo.getContact().getUrl(),
                                    docketInfo.getContact().getEmail().isEmpty() ? swaggerProperties.getContact().getEmail() : docketInfo.getContact().getEmail()
                            )
                    )
                    .termsOfServiceUrl(docketInfo.getTermsOfServiceUrl().isEmpty() ? swaggerProperties.getTermsOfServiceUrl() : docketInfo.getTermsOfServiceUrl())
                    .build();

            //如果等于空的话，这里设置groupName
            String documentGroupName = docketInfo.getGroup();
            if ("".equals(documentGroupName)) {
                documentGroupName = groupName;
            }

            //全局参数配置
            List<Parameter> parameters = assemblyGlobalOperationParameters(
                    swaggerProperties.getGlobalOperationParameters(),
                    docketInfo.getGlobalOperationParameters());

            Docket docket = new Docket(DocumentationType.SWAGGER_2)
                    .enable(swaggerProperties.getEnabled())
                    .host(swaggerProperties.getHost())
                    .apiInfo(apiInfo)
                    .groupName(documentGroupName)
                    .globalOperationParameters(parameters)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage(docketInfo.getBasePackage()))
                    //加了ApiOperation注解的类，才生成接口文档
                    .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                    .paths(PathSelectors.regex(docketInfo.getPathRegex()))
                    .build();

            configurableBeanFactory.registerSingleton(groupName, docket);
            docketList.add(docket);
        }
        return docketList;
    }


    /**
     * 创建 Docket对象
     *
     * @param swaggerProperties swagger配置
     * @return {@link Docket}
     */
    private Docket createDocket(SwaggerProperties swaggerProperties) {
        //API 基础信息
        ApiInfo apiInfo = new ApiInfoBuilder()
                .description(swaggerProperties.getDescription())
                .version(swaggerProperties.getVersion())
                .license(swaggerProperties.getLicense())
                .licenseUrl(swaggerProperties.getLicenseUrl())
                .contact(new Contact(swaggerProperties.getContact().getName(),
                        swaggerProperties.getContact().getUrl(),
                        swaggerProperties.getContact().getEmail()))
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerProperties.getEnabled())
                .host(swaggerProperties.getHost())
                .apiInfo(apiInfo)
                .groupName(swaggerProperties.getGroup())
                .globalOperationParameters(
                        buildGlobalOperationParametersFromSwaggerProperties(
                                swaggerProperties.getGlobalOperationParameters()))
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                //加了ApiOperation注解的类，才生成接口文档
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.regex(swaggerProperties.getPathRegex()))
                .build();
    }

    /**
     * 全局参数配置
     *
     * @param globalOperationParameters
     * @return {@link List<Parameter>}
     */
    private List<Parameter> buildGlobalOperationParametersFromSwaggerProperties(
            List<SwaggerProperties.GlobalOperationParameter> globalOperationParameters
    ) {

        List<Parameter> parameters = new ArrayList<>();

        if (Objects.isNull(globalOperationParameters)) {
            return parameters;
        }

        globalOperationParameters.forEach(globalOperationParameter -> {
            parameters.add(new ParameterBuilder()
                    .name(globalOperationParameter.getName())  //参数名
                    .description(globalOperationParameter.getDescription()) //描述信息
                    .modelRef(new ModelRef(globalOperationParameter.getModelRef())) //指定参数类型
                    .parameterType(globalOperationParameter.getParameterType()) //参数放在哪个地方:header,query,path,body.form
                    .required(globalOperationParameter.getRequired()) //参数是否必须传
                    .defaultValue(globalOperationParameter.getDefaultValue())  //默认值
                    .allowEmptyValue(globalOperationParameter.getAllowEmptyValue()) //允许为空
                    .order(globalOperationParameter.getOrder())  //排序
                    .build());
        });

        return parameters;
    }

    /**
     * 局部参数按照name覆盖局部参数
     *
     * @param globalOperationParameters
     * @param docketOperationParameters
     * @return {@link List<Parameter>}
     */
    private List<Parameter> assemblyGlobalOperationParameters(
            List<SwaggerProperties.GlobalOperationParameter> globalOperationParameters,
            List<SwaggerProperties.GlobalOperationParameter> docketOperationParameters
    ) {

        //如果局部的为空那么就走全局的
        if (Objects.isNull(docketOperationParameters) || docketOperationParameters.isEmpty()) {
            return buildGlobalOperationParametersFromSwaggerProperties(globalOperationParameters);
        }

        //获取参数名集合
        Set<String> docketNames = docketOperationParameters.stream()
                .map(SwaggerProperties.GlobalOperationParameter::getName)
                .collect(Collectors.toSet());

        List<SwaggerProperties.GlobalOperationParameter> resultOperationParameters = new ArrayList<>();

        //如果全局的不为null
        if (Objects.nonNull(globalOperationParameters)) {
            //循环全局
            for (SwaggerProperties.GlobalOperationParameter parameter : globalOperationParameters) {
                //判断全局的和局部是否有重复，如果不包含那就是添加
                if (!docketNames.contains(parameter.getName())) {
                    resultOperationParameters.add(parameter);
                }
            }
        }

        //合并
        resultOperationParameters.addAll(docketOperationParameters);
        //之后再去构建
        return buildGlobalOperationParametersFromSwaggerProperties(resultOperationParameters);
    }

    /**
     * 配置是否是生成模式
     *
     * @return {@link FilterRegistrationBean<ProductionSecurityFilter>}
     */
    @Bean
    public FilterRegistrationBean<ProductionSecurityFilter> myProductionSecurityFilter() {
        FilterRegistrationBean<ProductionSecurityFilter> registration = new FilterRegistrationBean<>(new ProductionSecurityFilter());
        Map<String, String> initParams = new HashMap<>(16);
        //这里取是否开启文档
        initParams.put("production", String.valueOf(swaggerProperties.getProduction()));
        registration.setInitParameters(initParams);
        registration.addUrlPatterns("/*");
        registration.setName("productionSecurityFilter");
        return registration;
    }

    /**
     * 配置认证
     *
     * @return {@link FilterRegistrationBean<SecurityBasicAuthFilter>}
     */
    @Bean
    public FilterRegistrationBean<SecurityBasicAuthFilter> mySecurityBasicAuthFilter() {
        FilterRegistrationBean<SecurityBasicAuthFilter> registration = new FilterRegistrationBean<>(new SecurityBasicAuthFilter());

        Map<String, String> initParams = new HashMap<>(16);
        initParams.put("enableBasicAuth", String.valueOf(swaggerProperties.getBasic().getEnable()));
        initParams.put("userName", swaggerProperties.getBasic().getUsername());
        initParams.put("password", swaggerProperties.getBasic().getPassword());
        registration.setInitParameters(initParams);

        registration.addUrlPatterns("/*");
        registration.setName("securityBasicAuthFilter");
        return registration;
    }

}
