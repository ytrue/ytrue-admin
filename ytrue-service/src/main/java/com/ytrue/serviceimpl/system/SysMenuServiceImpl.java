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
import com.ytrue.infra.core.response.ResponseCodeEnum;
import com.ytrue.infra.core.response.ServerResponseCode;
import com.ytrue.infra.core.util.AssertUtil;
import com.ytrue.infra.core.util.BeanUtils;
import com.ytrue.infra.db.base.BaseServiceImpl;
import com.ytrue.infra.db.dao.system.SysMenuDao;
import com.ytrue.infra.db.dao.system.SysRoleMenuDao;
import com.ytrue.infra.db.dao.system.SysUserDao;
import com.ytrue.service.system.SysMenuService;
import com.ytrue.tools.query.util.QueryHelp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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
        LambdaQueryWrapper<SysMenu> queryWrapper = QueryHelp.<SysMenu>lambdaQueryWrapperBuilder(queryParam)
                .orderByAsc(SysMenu::getMenuSort)
                .orderByDesc(SysMenu::getId);

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
        AssertUtil.numberNotEquals(requestParam.getId(), requestParam.getPid(), ServerResponseCode.error("父级不能是自己"));

        SysMenu sysMenu = this.getById(requestParam.getId());
        AssertUtil.notNull(sysMenu, ResponseCodeEnum.ILLEGAL_OPERATION);

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
    public void removeBatchBySysMenuIds(List<Long> ids) {
        for (Long id : ids) {
            // 校验是否存在子级
            SysMenu childMenu = lambdaQuery().eq(SysMenu::getPid, id).one();
            AssertUtil.isNull(childMenu, ServerResponseCode.error("存在子级，请解除后再试"));

            // 校验角色绑定或者去解绑角色的关系
            SysRoleMenu sysRoleMenu = sysRoleMenuDao.selectOne(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getMenuId, id));
            AssertUtil.isNull(sysRoleMenu, ServerResponseCode.error("存在角色关联，请解除后再试"));

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


    /**
     * 根据用户ID查询菜单
     *
     * @return
     */
    @Override
    public List<Tree<String>> listMenuTreeBySysUserId(Long userId) {
        // 获取菜单
        List<SysMenu> menus = listBySysUserId(userId);

        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        //转换器
        return TreeUtil.build(menus, "0", treeNodeConfig, (sysMenu, tree) -> {
            tree.setId(String.valueOf(sysMenu.getId()));
            tree.setParentId(String.valueOf(sysMenu.getPid()));
            tree.setName(sysMenu.getPath());

            // 不是外链，并且是M path要加/
            String path = sysMenu.getPath();
            if (MenuTypeEnum.DIRECTORY.getType().equals(sysMenu.getMenuType()) && !sysMenu.getIsFrame()) {
                path = "/" + path;
            }
            tree.putExtra("path", path);

            // 扩展属性 ...
            tree.putExtra("query", sysMenu.getQuery());
            tree.putExtra("hidden", !sysMenu.getVisible());

            tree.putExtra("component", getComponent(sysMenu));
            // 如果是ParentView path要去掉/
            if (getComponent(sysMenu).equals(ComponentTypeEnum.PARENT_VIEW.getType())) {
                tree.putExtra("path", sysMenu.getPath());
            }

            // 有子菜单并且类型是M
            if (sysMenu.getSubCount() > 0 && MenuTypeEnum.DIRECTORY.getType().equals(sysMenu.getMenuType())) {
                tree.putExtra("alwaysShow", true);
                tree.putExtra("redirect", "noRedirect");
            }
            HashMap<String, Object> meta = new HashMap<>(8);
            meta.put("title", sysMenu.getMenuName());
            meta.put("icon", sysMenu.getIcon());
            meta.put("noCache", !sysMenu.getIsCache());
            meta.put("link", null);
            if (sysMenu.getIsFrame()) {
                meta.put("link", sysMenu.getPath());
            }
            tree.putExtra("meta", meta);
        });
    }

    @Override
    public List<SysMenu> listBySysUserId(Long userId) {
        SysUser sysUser = sysUserDao.selectById(userId);
        AssertUtil.notNull(sysUser, ResponseCodeEnum.DATA_NOT_FOUND);

        // 平台账号,但是是超级管理员
        if (sysUser.getAdmin()) {
            return lambdaQuery().eq(SysMenu::getStatus, 1)
                    .in(SysMenu::getMenuType, MenuTypeEnum.DIRECTORY.getType(), MenuTypeEnum.MENU.getType())
                    .orderByAsc(SysMenu::getPid)
                    .orderByAsc(SysMenu::getMenuSort)
                    .list();
        }

        // 不然就是普通的账号
        return baseMapper.selectMenusBySysUserId(userId);
    }

    @Override
    public SysMenuIdResp getBySysMenuId(Long id) {
        SysMenu data = this.getById(id);
        AssertUtil.notNull(data, ResponseCodeEnum.DATA_NOT_FOUND);
        return BeanUtils.copyProperties(data, SysMenuIdResp::new);
    }


    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    private String getComponent(SysMenu menu) {

        if (StrUtil.isNotEmpty(menu.getComponent())) {
            return menu.getComponent();
        }
        // 默认LAYOUT
        String component = ComponentTypeEnum.LAYOUT.getType();

        if (menu.getPid().intValue() != 0 && menu.getIsFrame()) {
            component = ComponentTypeEnum.INNER_LINK.getType();
        } else if (menu.getPid().intValue() != 0 && MenuTypeEnum.DIRECTORY.getType().equals(menu.getMenuType())) {
            component = ComponentTypeEnum.PARENT_VIEW.getType();
        }
        return component;
    }
}
