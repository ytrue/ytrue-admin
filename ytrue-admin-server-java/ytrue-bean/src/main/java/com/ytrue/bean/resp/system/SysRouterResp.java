package com.ytrue.bean.resp.system;

import com.ytrue.infra.core.constant.StrPool;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
public class SysRouterResp {

    private Long id;

    @Schema(description = "路由名字")
    private String name;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现")
    private boolean hidden;

    @Schema(description = "重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击")
    private String redirect;

    @Schema(description = "组件地址")
    private String component;

    @Schema(description = "路由参数：如 {\"id\": 1, \"name\": \"ry\"}")
    private String query;

    @Schema(description = "当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面")
    private Boolean alwaysShow;

    @Schema(description = "其他元素")
    private SysRouterMetaResp meta;

    @Schema(description = "子路由")
    private List<SysRouterResp> children;


    @Data
    @NoArgsConstructor
    public static class SysRouterMetaResp {

        public SysRouterMetaResp(String title, String icon) {
            this.title = title;
            this.icon = icon;
        }

        public SysRouterMetaResp(String title, String icon, String link) {
            this.title = title;
            this.icon = icon;
            this.link = link;
        }

        public SysRouterMetaResp(String title, String icon, boolean noCache, String link) {
            this.title = title;
            this.icon = icon;
            this.noCache = noCache;

            boolean isHttp = StringUtils.startsWithAny(link, StrPool.HTTP, StrPool.HTTPS);
            if (isHttp) {
                this.link = link;
            }
        }


        @Schema(description = "设置该路由在侧边栏和面包屑中展示的名字")
        private String title;

        @Schema(description = "设置该路由的图标，对应路径src/assets/icons/svg")
        private String icon;

        @Schema(description = "设置为true，则不会被 <keep-alive>缓存")
        private boolean noCache;

        @Schema(description = "内链地址（http(s)://开头）")
        private String link;
    }
}
