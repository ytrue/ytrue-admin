package com.ytrue.service.system;

import cn.hutool.core.lang.tree.Tree;
import com.ytrue.infra.db.base.IBaseService;
import com.ytrue.bean.dataobject.system.SysMenu;

import java.util.List;

/**
 * @author ytrue
 * @description: SysMenuService
 * @date 2022/12/7 14:11
 */
public interface SysMenuService extends IBaseService<SysMenu> {

    /**
     * 新增菜单
     *
     * @param sysMenu
     */
    void addMenu(SysMenu sysMenu);

    /**
     * 修改菜单
     *
     * @param sysMenu
     */
    void updateMenu(SysMenu sysMenu);

    /**
     * 删除菜单
     *
     * @param ids
     */
    void removeBatchMenu(List<Long> ids);

    /**
     * 获取菜单树形结构，根据用户id
     *
     * @param userId
     * @return
     */
    List<Tree<String>> listMenuTreeBySysUserId(Long userId);

    /**
     * 根据用户id获取菜单列表
     *
     * @param userId
     * @return
     */
    List<SysMenu> listBySysUserId(Long userId);
}
