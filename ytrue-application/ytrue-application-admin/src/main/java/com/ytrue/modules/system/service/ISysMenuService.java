package com.ytrue.modules.system.service;

import cn.hutool.core.lang.tree.Tree;
import com.ytrue.common.base.IBaseService;
import com.ytrue.modules.system.model.SysMenu;

import java.util.List;

/**
 * @author ytrue
 * @description: ISysMenuService
 * @date 2022/12/7 14:11
 */
public interface ISysMenuService extends IBaseService<SysMenu> {

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
    public List<Tree<String>> listMenuTreeByUserId(Long userId);
}
