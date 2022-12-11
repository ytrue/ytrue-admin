package com.ytrue.tools.document;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ytrue
 * @date 2022/4/22 10:16
 * @description 配置属性类，用于封装yml配置文件中关于接口文档相关的配置信息
 */
@Data
@ConfigurationProperties(prefix = "ytrue.document")
public class SwaggerProperties {

    /**
     * 是否开启swagger
     **/
    private Boolean enabled = false;

    /**
     * 是否生产环境
     */
    private Boolean production = true;

    /**
     * 标题
     **/
    private String title = "yadmin";

    /**
     * 描述
     **/
    private String description = "在线文档";

    /**
     * 版本
     **/
    private String version = "1.0";

    /**
     * 自定义组名
     */
    private String group = "default";

    /**
     * 许可证
     **/
    private String license = "";
    /**
     * 许可证URL
     **/
    private String licenseUrl = "";
    /**
     * 服务条款URL
     **/
    private String termsOfServiceUrl = "";

    /**
     * 联系人信息
     */
    private Contact contact = new Contact();

    /**
     * swagger会解析的包路径
     **/
    private String basePackage = "com.ytrue";

    /**
     * swagger会解析的url规则
     **/
    private String pathRegex = "/.*";

    /**
     * 分组文档
     **/
    private Map<String, DocketInfo> docket = new LinkedHashMap<>();


    /**
     * host信息
     **/
    private String host = "";

    /**
     * 排序
     */
    private Integer order = 1;

    /**
     * 访问账号密码
     */
    private Basic basic = new Basic();

    /**
     * 全局参数配置,这个暂时不做
     **/
    private List<GlobalOperationParameter> globalOperationParameters;


    /**
     * 联系人
     */
    @Data
    public static class Contact {
        /**
         * 联系人
         **/
        private String name = "ytrue";
        /**
         * 联系人url
         **/
        private String url = "https://github.com/ytrue";
        /**
         * 联系人email
         **/
        private String email = "ytrue@qq.com";
    }

    /**
     * 分组文档
     */
    @Data
    public static class DocketInfo {
        /**
         * 标题
         **/
        private String title = "yadmin";

        /**
         * 描述
         **/
        private String description = "在线文档";

        /**
         * 版本
         **/
        private String version = "1.0";

        /**
         * 自定义组名
         */
        private String group = "";

        /**
         * 许可证
         **/
        private String license = "";
        /**
         * 许可证URL
         **/
        private String licenseUrl = "";
        /**
         * 服务条款URL
         **/
        private String termsOfServiceUrl = "";

        /**
         * 联系人信息
         */
        private Contact contact = new Contact();

        /**
         * swagger会解析的包路径
         **/
        private String basePackage = "";

        /**
         * swagger会解析的url规则
         **/
        private String pathRegex = "/.*";

        /**
         * 全局参数配置
         */
        private List<GlobalOperationParameter> globalOperationParameters;


        /**
         * 排序
         */
        private Integer order = 1;

        public String getGroup() {
            if (group == null || "".equals(group)) {
                return title;
            }
            return group;
        }
    }


    /**
     * 全局配置
     */
    @Setter
    @Getter
    public static class GlobalOperationParameter {
        /**
         * 参数名
         **/
        private String name;

        /**
         * 描述信息
         **/
        private String description = "全局参数";

        /**
         * 指定参数类型
         **/
        private String modelRef = "String";

        /**
         * 参数放在哪个地方:header,query,path,body.form
         **/
        private String parameterType = "header";

        /**
         * 参数是否必须传
         **/
        private Boolean required = false;
        /**
         * 默认值
         */
        private String defaultValue = "";
        /**
         * 允许为空
         */
        private Boolean allowEmptyValue = true;
        /**
         * 排序
         */
        private int order = 1;
    }

    /**
     * 认证配置
     */
    @Data
    public static class Basic {
        private Boolean enable = false;
        private String username = "ytrue";
        private String password = "ytrue";
    }
}
