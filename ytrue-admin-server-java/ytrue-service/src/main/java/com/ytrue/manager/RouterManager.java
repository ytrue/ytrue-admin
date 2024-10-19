package com.ytrue.manager;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.StrUtil;
import com.ytrue.bean.dataobject.system.SysMenu;
import com.ytrue.bean.enums.system.ComponentTypeEnum;
import com.ytrue.bean.enums.system.MenuTypeEnum;
import com.ytrue.bean.resp.system.SysRouterResp;
import com.ytrue.infra.core.constant.StrPool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class RouterManager {

    /**
     * 构建路由
     * @param treeList
     * @return
     */
    public List<SysRouterResp> buildSysRouters(List<Tree<String>> treeList) {
        List<SysRouterResp> routers = new LinkedList<>();

        treeList.forEach(tree -> {
            // 获取menu
            SysMenu sysMenu = (SysMenu) tree.get("menu");

            SysRouterResp router = new SysRouterResp();
            // 前端组件
            router.setComponent(getComponent(sysMenu));
            // 是否隐藏 true是隐藏，false不隐藏
            router.setHidden(!sysMenu.getVisible());
            // id
            router.setId(sysMenu.getId());
            // 路由名称
            router.setName(getRouteName(sysMenu));
            // 访问path
            router.setPath(getRouterPath(sysMenu));
            // 扩展属性 ...
            router.setQuery(sysMenu.getQuery());
            SysRouterResp.SysRouterMetaResp meta = new SysRouterResp.SysRouterMetaResp(
                    sysMenu.getMenuName(),
                    sysMenu.getIcon(),
                    !sysMenu.getIsCache(),
                    sysMenu.getPath()
            );
            router.setMeta(meta);

            // 获取子级
            List<Tree<String>> childrenList = tree.getChildren();
            // 有子菜单并且类型是目录
            if (sysMenu.getSubCount() > 0 && MenuTypeEnum.DIRECTORY.getType().equals(sysMenu.getMenuType())) {
                router.setRedirect("noRedirect");
                router.setAlwaysShow(true);
                router.setChildren(buildSysRouters(childrenList));
            }

            // 是否为菜单内部跳转
            if (isMenuFrame(sysMenu)) {
                router.setMeta(null);

                SysRouterResp children = new SysRouterResp();
                children.setPath(sysMenu.getPath());
                children.setComponent(sysMenu.getComponent());
                children.setName(StringUtils.capitalize(sysMenu.getPath()));
                children.setQuery(sysMenu.getQuery());
                SysRouterResp.SysRouterMetaResp childrenMeta = new SysRouterResp.SysRouterMetaResp(
                        sysMenu.getMenuName(),
                        sysMenu.getIcon(),
                        !sysMenu.getIsCache(),
                        sysMenu.getPath()
                );
                children.setMeta(childrenMeta);
                router.setChildren(List.of(children));
            }

            // 如果pid等于0，并且是否为内链组件
            if (sysMenu.getPid().intValue() == 0 && isInnerLink(sysMenu)) {
                router.setMeta(new SysRouterResp.SysRouterMetaResp(sysMenu.getMenuName(), sysMenu.getIcon()));
                router.setPath(StrPool.SLASH);

                SysRouterResp children = new SysRouterResp();
                String routerPath = innerLinkReplaceEach(sysMenu.getPath());
                children.setPath(routerPath);
                children.setComponent(ComponentTypeEnum.INNER_LINK.getType());
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new SysRouterResp.SysRouterMetaResp(
                        sysMenu.getMenuName(),
                        sysMenu.getIcon(),
                        sysMenu.getPath()
                ));
                router.setChildren(List.of(children));
            }
            routers.add(router);
        });


        return routers;
    }


    /**
     * 获取路由名称
     * @param menu
     * @return
     */
    private String getRouteName(SysMenu menu) {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 是否为菜单内部跳转
     * @param menu
     * @return
     */
    private boolean isMenuFrame(SysMenu menu) {
        // pid等于0，并且是 C 并且 不是外链
        return menu.getPid().intValue() == 0 && MenuTypeEnum.MENU.getType().equals(menu.getMenuType()) && !menu.getIsFrame();
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    private boolean isInnerLink(SysMenu menu) {
        boolean isHttp = StringUtils.startsWithAny(menu.getPath(), StrPool.HTTP, StrPool.HTTPS);
        return !menu.getIsFrame() && isHttp;
    }

    /**
     * 内链域名特殊字符替换
     *
     * @return 替换后的内链域名
     */
    private String innerLinkReplaceEach(String path) {
        // new String[] {"http://", "https://", "www." ".", ":" } new String[] { "", "", "", "/", "/" }
        return StringUtils.replaceEach(path, new String[]{StrPool.HTTP, StrPool.HTTPS, StrPool.WWW, StrPool.DOT, StrPool.COLON}, new String[]{StrPool.EMPTY_STRING, StrPool.EMPTY_STRING, StrPool.EMPTY_STRING, StrPool.SLASH, StrPool.SLASH});
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    private boolean isParentView(SysMenu menu) {
        return menu.getPid().intValue() != 0 && MenuTypeEnum.DIRECTORY.getType().equals(menu.getMenuType());
    }

    /**
     * 获取路由地址
     * @param menu
     * @return
     */
    private String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getPid().intValue() != 0 && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }

        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getPid().intValue() && MenuTypeEnum.DIRECTORY.getType().equals(menu.getMenuType()) && !menu.getIsFrame()) {
            // 没有 / 才添加
            if (!routerPath.startsWith(StrPool.SLASH)) {
                routerPath = StrPool.SLASH + menu.getPath();
            }
        }
        // 非外链并且是一级目录（类型为菜单）
        if (isMenuFrame(menu)) {
            routerPath = StrPool.SLASH;
        }

        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    private String getComponent(SysMenu menu) {

        // 如果组件不为空，就返回内容里面的组件,并且不是为菜单内部跳转
        if (StrUtil.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            return menu.getComponent();
        }

        // 组件为空，pid不等于0，是是否为内链组件
        if (StrUtil.isEmpty(menu.getComponent()) && menu.getPid().intValue() != 0 && isInnerLink(menu)) {
            return ComponentTypeEnum.INNER_LINK.getType();
        }

        if (StrUtil.isEmpty(menu.getComponent()) && isParentView(menu)) {
            return ComponentTypeEnum.PARENT_VIEW.getType();
        }

        // 不然返回的前端组件是 Layout
        return ComponentTypeEnum.LAYOUT.getType();
    }
}
