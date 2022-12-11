package com.ytrue.modules.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.ytrue.common.base.BaseServiceImpl;
import com.ytrue.common.enums.ResponseCode;
import com.ytrue.common.utils.AssertUtils;
import com.ytrue.modules.system.dao.SysMenuDao;
import com.ytrue.modules.system.dao.SysUserDao;
import com.ytrue.modules.system.model.SysMenu;
import com.ytrue.modules.system.model.SysUser;
import com.ytrue.modules.system.service.ISysMenuService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuDao, SysMenu> implements ISysMenuService {

    private final SysUserDao sysUserDao;

    private final SysMenuDao sysMenuDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addMenu(SysMenu sysMenu) {
        save(sysMenu);
        updateSubCnt(sysMenu.getPid());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMenu(SysMenu sysMenu) {
        AssertUtils.noteEquals(sysMenu.getId(), sysMenu.getPid(), ResponseCode.PARENT_EQ_ITSELF);

        // 旧的菜单
        Long oldPid = getById(sysMenu.getId()).getPid();
        Long newPid = sysMenu.getPid();

        updateById(sysMenu);
        // 更新父节点中子节点数目
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
    }

    @Override
    public void removeBatchMenu(List<Long> ids) {
        for (Long id : ids) {
            SysMenu childMenu = lambdaQuery().eq(SysMenu::getPid, id).one();
            AssertUtils.isNull(childMenu, ResponseCode.HAS_CHILD);

            //  TODO 校验角色绑定或者去解绑角色的关系

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
    private void updateSubCnt(Long menuId) {
        if (menuId == null) {
            return;
        }
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
    public List<Tree<String>> listMenuTreeByUserId(Long userId) {
        SysUser sysUser = sysUserDao.selectById(userId);
        AssertUtils.notNull(sysUser, ResponseCode.DATA_NOT_FOUND);

        List<SysMenu> menus;
        if (sysUser.getIsAdmin()) {
            menus = lambdaQuery().eq(SysMenu::getStatus, 1).in(SysMenu::getMenuType, "M", "C").orderByAsc(SysMenu::getPid).orderByAsc(SysMenu::getMenuSort).list();
        } else {
            // 根据用户查询
            menus = sysMenuDao.selectMenuTreeByUserId(userId);
        }

        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        //转换器
        return TreeUtil.build(menus, "0", treeNodeConfig, (sysMenu, tree) -> {
            tree.setId(Convert.toStr(sysMenu.getId()));
            tree.setParentId(Convert.toStr(sysMenu.getPid()));
            tree.setName(sysMenu.getPath());

            // 不是外链，并且是M path要加/
            String path = sysMenu.getPath();
            if ("M".equals(sysMenu.getMenuType()) && !sysMenu.getIsFrame()) {
                path = "/" + path;
            }
            tree.putExtra("path", path);

            // 扩展属性 ...
            tree.putExtra("hidden", !sysMenu.getVisible());
            tree.putExtra("component", sysMenu.getComponent());
            // 有子菜单并且类型是M
            if (sysMenu.getSubCount() > 0 && "M".equals(sysMenu.getMenuType())) {
                tree.putExtra("alwaysShow", true);
                tree.putExtra("redirect", "noRedirect");
            }
            HashMap<String, Object> meta = new HashMap<>(16);
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
}
