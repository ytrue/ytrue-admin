package com.ytrue.serviceimpl.system;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.bean.dataobject.system.SysMenu;
import com.ytrue.bean.dataobject.system.SysRoleMenu;
import com.ytrue.bean.dataobject.system.SysUser;
import com.ytrue.bean.enums.system.ComponentTypeEnum;
import com.ytrue.bean.enums.system.MenuTypeEnum;
import com.ytrue.bean.query.system.SysMenuListQuery;
import com.ytrue.bean.req.system.SysMenuAddReq;
import com.ytrue.bean.req.system.SysMenuUpdateReq;
import com.ytrue.bean.resp.system.SysMenuIdResp;
import com.ytrue.bean.resp.system.SysRouterResp;
import com.ytrue.dao.system.SysMenuDao;
import com.ytrue.dao.system.SysRoleMenuDao;
import com.ytrue.dao.system.SysUserDao;
import com.ytrue.infra.core.constant.StrPool;
import com.ytrue.infra.core.response.ServerResponseInfo;
import com.ytrue.infra.core.response.ServerResponseInfoEnum;
import com.ytrue.infra.core.util.AssertUtil;
import com.ytrue.infra.core.util.BeanUtils;
import com.ytrue.infra.db.base.BaseServiceImpl;
import com.ytrue.infra.db.query.util.QueryHelp;
import com.ytrue.service.system.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ytrue
 * @description: SysMenuServiceImpl
 * @date 2022/12/7 14:11
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuDao, SysMenu> implements SysMenuService {

    private final SysUserDao sysUserDao;

    private final SysRoleMenuDao sysRoleMenuDao;

    @Override
    public List<SysMenu> listBySysMenuListQuery(SysMenuListQuery queryParam) {
        LambdaQueryWrapper<SysMenu> queryWrapper = QueryHelp.<SysMenu>builderlambdaQueryWrapper(queryParam).orderByAsc(SysMenu::getMenuSort).orderByDesc(SysMenu::getId);

        return this.list(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addMenu(SysMenuAddReq requestParam) {
        // 转换bean
        SysMenu sysMenu = BeanUtils.copyProperties(requestParam, SysMenu::new);

        save(sysMenu);
        updateSubCnt(sysMenu.getPid());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMenu(SysMenuUpdateReq requestParam) {
        AssertUtil.numberNotEquals(requestParam.getId(), requestParam.getPid(), ServerResponseInfo.error("父级不能是自己"));

        SysMenu sysMenu = this.getById(requestParam.getId());
        AssertUtil.notNull(sysMenu, ServerResponseInfoEnum.ILLEGAL_OPERATION);

        // 旧的菜单
        Long oldPid = sysMenu.getPid();
        Long newPid = requestParam.getPid();

        // bean转换
        BeanUtils.copyProperties(requestParam, sysMenu);

        updateById(sysMenu);
        // 更新父节点中子节点数目
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
    }


    @Override
    public void removeBySysMenuIds(List<Long> ids) {
        for (Long id : ids) {
            // 校验是否存在子级
            SysMenu childMenu = lambdaQuery().eq(SysMenu::getPid, id).one();
            AssertUtil.isNull(childMenu, ServerResponseInfo.error("存在子级，请解除后再试"));

            // 校验角色绑定或者去解绑角色的关系
            SysRoleMenu sysRoleMenu = sysRoleMenuDao.selectOne(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getMenuId, id));
            AssertUtil.isNull(sysRoleMenu, ServerResponseInfo.error("存在角色关联，请解除后再试"));

            SysMenu menu = getById(id);
            removeById(id);
            updateSubCnt(menu.getPid());
        }
    }


    /**
     * 更新数量
     *
     * @param menuId
     */
    private void updateSubCnt(long menuId) {
        Long count = lambdaQuery().eq(SysMenu::getPid, menuId).count();
        // 更新
        lambdaUpdate().eq(SysMenu::getId, menuId).set(SysMenu::getSubCount, count).update();
    }

    @Override
    public List<SysRouterResp> listSysRouterBySysUserId(Long userId) {
        // 获取菜单
        List<SysMenu> menus = listBySysUserId(userId);
        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        //转换器
        List<Tree<String>> treeList = TreeUtil.build(menus, "0", treeNodeConfig, (sysMenu, tree) -> {
            // id
            tree.setId(String.valueOf(sysMenu.getId()));
            // 父id
            tree.setParentId(String.valueOf(sysMenu.getPid()));
            // 路由名称
            tree.setName(sysMenu.getMenuName());
            // 整个塞进去
            tree.putExtra("menu", sysMenu);
        });

        return this.buildSysRouters(treeList);
    }

    /**
     * 构建路由
     * @param treeList
     * @return
     */
    private List<SysRouterResp> buildSysRouters(List<Tree<String>> treeList) {
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
            SysRouterResp.SysRouterMetaResp meta = new SysRouterResp.SysRouterMetaResp();
            meta.setIcon(sysMenu.getIcon());
            meta.setTitle(sysMenu.getMenuName());
            meta.setNoCache(!sysMenu.getIsCache());
            meta.setLink(sysMenu.getPath());
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
                SysRouterResp.SysRouterMetaResp childrenMeta = new SysRouterResp.SysRouterMetaResp(sysMenu.getMenuName(), sysMenu.getIcon(), !sysMenu.getIsCache(), sysMenu.getPath());
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
                children.setMeta(new SysRouterResp.SysRouterMetaResp(sysMenu.getMenuName(), sysMenu.getIcon(), sysMenu.getPath()));
                router.setChildren(List.of(children));
            }
            routers.add(router);
        });


        return routers;
    }


    @Override
    public List<SysMenu> listBySysUserId(Long userId) {
        SysUser sysUser = sysUserDao.selectById(userId);
        AssertUtil.notNull(sysUser, ServerResponseInfoEnum.NOT_FOUND);

        // 平台账号,但是是超级管理员
        if (sysUser.getAdmin()) {
            return lambdaQuery().eq(SysMenu::getStatus, 1).in(SysMenu::getMenuType, MenuTypeEnum.DIRECTORY.getType(), MenuTypeEnum.MENU.getType()).orderByAsc(SysMenu::getPid).orderByAsc(SysMenu::getMenuSort).list();
        }

        // 不然就是普通的账号
        return baseMapper.selectMenusBySysUserId(userId);
    }

    @Override
    public SysMenuIdResp getBySysMenuId(Long id) {
        SysMenu data = this.getById(id);
        AssertUtil.notNull(data, ServerResponseInfoEnum.NOT_FOUND);
        return BeanUtils.copyProperties(data, SysMenuIdResp::new);
    }


    /**
     * 获取路由名称
     * @param menu
     * @return
     */
    public String getRouteName(SysMenu menu) {
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
    public boolean isMenuFrame(SysMenu menu) {
        // pid等于0，并且是 C 并且 不是外链
        return menu.getPid().intValue() == 0 && MenuTypeEnum.MENU.getType().equals(menu.getMenuType()) && !menu.getIsFrame();
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenu menu) {
        boolean isHttp = StringUtils.startsWithAny(menu.getPath(), StrPool.HTTP, StrPool.HTTPS);
        return !menu.getIsFrame() && isHttp;
    }

    /**
     * 内链域名特殊字符替换
     *
     * @return 替换后的内链域名
     */
    public String innerLinkReplaceEach(String path) {
        // new String[] {"http://", "https://", "www." ".", ":" } new String[] { "", "", "", "/", "/" }
        return StringUtils.replaceEach(path, new String[]{StrPool.HTTP, StrPool.HTTPS, StrPool.WWW, StrPool.DOT, StrPool.COLON}, new String[]{StrPool.EMPTY, StrPool.EMPTY, StrPool.EMPTY, StrPool.SLASH, StrPool.SLASH});
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu) {
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
