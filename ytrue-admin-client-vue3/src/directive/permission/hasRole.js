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
        const super_admin = "admin" // 超级管理员角色，拥有最终权限
        const roles = useUserStore().roles // 从 store 获取当前用户角色

        // 检查 value 是否是一个非空数组
        if (value && value instanceof Array && value.length > 0) {
            const roleFlag = value // 需要的角色列表

            // 判断用户是否拥有所需角色或是超级管理员
            const hasRole = roles.some(role => {
                return super_admin === role || roleFlag.includes(role)
            })

            // 如果用户没有所需角色，则移除元素
            if (!hasRole) {
                el.parentNode && el.parentNode.removeChild(el)
            }
        } else {
            // 如果角色权限值未设置，抛出错误
            throw new Error(`请设置角色权限标签值`)
        }
    }
}
