import useUserStore from '@/store/modules/user'

export default {
    /**
     * 指令钩子：mounted
     * 当指令绑定到元素上时执行
     * @param {HTMLElement} el - 绑定指令的元素
     * @param {Object} binding - 指令绑定对象
     * @param {Object} vnode - 与元素对应的虚拟节点
     */
    mounted(el, binding, vnode) {
        const {value} = binding
        const all_permission = "*:*:*" // 代表拥有所有权限的标识
        const permissions = useUserStore().permissions // 从 store 获取当前用户的权限
        // 检查 value 是否是一个非空数组
        if (value && value instanceof Array && value.length > 0) {
            const permissionFlag = value // 需要的权限列表

            // 判断用户是否拥有所需权限或是否是全权限标识
            const hasPermissions = permissions.some(permission => {
                return all_permission === permission || permissionFlag.includes(permission)
            })

            // 如果用户没有所需权限，则移除元素
            if (!hasPermissions) {
                el.parentNode && el.parentNode.removeChild(el)
            }
        } else {
            // 如果操作权限值未设置，抛出错误
            throw new Error(`请设置操作权限标签值`)
        }
    }
}
