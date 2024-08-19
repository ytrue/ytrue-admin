// 会话级缓存管理对象
const sessionCache = {
    /**
     * 设置会话存储中的值
     * @param {string} key - 存储项的键
     * @param {string} value - 存储项的值
     */
    set(key, value) {
        // 检查浏览器是否支持 sessionStorage
        if (!sessionStorage) {
            return
        }
        // 确保键和值都不为 null
        if (key != null && value != null) {
            sessionStorage.setItem(key, value)
        }
    },

    /**
     * 获取会话存储中的值
     * @param {string} key - 存储项的键
     * @returns {string | null} 返回存储的值，如果不存在则返回 null
     */
    get(key) {
        // 检查浏览器是否支持 sessionStorage
        if (!sessionStorage) {
            return null
        }
        // 如果键为 null，返回 null
        if (key == null) {
            return null
        }
        return sessionStorage.getItem(key)
    },

    /**
     * 设置会话存储中的 JSON 对象
     * @param {string} key - 存储项的键
     * @param {Object} jsonValue - 要存储的 JSON 对象
     */
    setJSON(key, jsonValue) {
        if (jsonValue != null) {
            // 将 JSON 对象转换为字符串并存储
            this.set(key, JSON.stringify(jsonValue))
        }
    },

    /**
     * 获取会话存储中的 JSON 对象
     * @param {string} key - 存储项的键
     * @returns {Object | null} 返回解析后的 JSON 对象，如果不存在则返回 null
     */
    getJSON(key) {
        const value = this.get(key)
        if (value != null) {
            // 解析 JSON 字符串并返回对象
            return JSON.parse(value)
        }
    },

    /**
     * 从会话存储中移除指定的项
     * @param {string} key - 存储项的键
     */
    remove(key) {
        sessionStorage.removeItem(key)
    }
}

// 本地缓存管理对象
const localCache = {
    /**
     * 设置本地存储中的值
     * @param {string} key - 存储项的键
     * @param {string} value - 存储项的值
     */
    set(key, value) {
        // 检查浏览器是否支持 localStorage
        if (!localStorage) {
            return
        }
        // 确保键和值都不为 null
        if (key != null && value != null) {
            localStorage.setItem(key, value)
        }
    },

    /**
     * 获取本地存储中的值
     * @param {string} key - 存储项的键
     * @returns {string | null} 返回存储的值，如果不存在则返回 null
     */
    get(key) {
        // 检查浏览器是否支持 localStorage
        if (!localStorage) {
            return null
        }
        // 如果键为 null，返回 null
        if (key == null) {
            return null
        }
        return localStorage.getItem(key)
    },

    /**
     * 设置本地存储中的 JSON 对象
     * @param {string} key - 存储项的键
     * @param {Object} jsonValue - 要存储的 JSON 对象
     */
    setJSON(key, jsonValue) {
        if (jsonValue != null) {
            // 将 JSON 对象转换为字符串并存储
            this.set(key, JSON.stringify(jsonValue))
        }
    },

    /**
     * 获取本地存储中的 JSON 对象
     * @param {string} key - 存储项的键
     * @returns {Object | null} 返回解析后的 JSON 对象，如果不存在则返回 null
     */
    getJSON(key) {
        const value = this.get(key)
        if (value != null) {
            // 解析 JSON 字符串并返回对象
            return JSON.parse(value)
        }
    },

    /**
     * 从本地存储中移除指定的项
     * @param {string} key - 存储项的键
     */
    remove(key) {
        localStorage.removeItem(key)
    }
}

export default {
    /**
     * 会话级缓存接口
     */
    session: sessionCache,
    /**
     * 本地缓存接口
     */
    local: localCache
}
