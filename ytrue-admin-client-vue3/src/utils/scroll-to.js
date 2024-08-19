/**
 * 二次方缓动函数，生成平滑的动画效果
 * @param {number} t - 当前时间（从0到持续时间）
 * @param {number} b - 起始值
 * @param {number} c - 变化量（终止值减去起始值）
 * @param {number} d - 持续时间
 * @returns {number} - 当前值
 */
Math.easeInOutQuad = function (t, b, c, d) {
    t /= d / 2
    if (t < 1) {
        return c / 2 * t * t + b
    }
    t--
    return -c / 2 * (t * (t - 2) - 1) + b
}

// 获取合适的请求动画帧函数
const requestAnimFrame = (function () {
    return window.requestAnimationFrame ||
        window.webkitRequestAnimationFrame ||
        window.mozRequestAnimationFrame ||
        function (callback) {
            window.setTimeout(callback, 1000 / 60)
        }
})()

/**
 * 平滑滚动到指定位置
 * @param {number} amount - 滚动的目标位置
 */
function move(amount) {
    // 设置文档的滚动位置
    document.documentElement.scrollTop = amount
    document.body.parentNode.scrollTop = amount
    document.body.scrollTop = amount
}

/**
 * 获取当前的滚动位置
 * @returns {number} - 当前的滚动位置
 */
function position() {
    // 返回文档当前的滚动位置
    return document.documentElement.scrollTop ||
        document.body.parentNode.scrollTop ||
        document.body.scrollTop
}

/**
 * 实现平滑滚动到指定位置的动画效果
 * @param {number} to - 目标滚动位置
 * @param {number} [duration=500] - 动画持续时间，默认为500毫秒
 * @param {Function} [callback] - 动画完成后的回调函数
 */
export function scrollTo(to, duration, callback) {
    // 获取当前滚动位置
    const start = position()
    // 计算滚动的总变化量
    const change = to - start
    // 定义每次更新的时间增量
    const increment = 20
    // 当前时间初始化为0
    let currentTime = 0
    // 如果未传递持续时间，则默认为500毫秒
    duration = (typeof (duration) === 'undefined') ? 500 : duration

    /**
     * 动画函数，用于平滑滚动
     */
    const animateScroll = function () {
        // 增加当前时间
        currentTime += increment
        // 使用二次方缓动函数计算当前值
        const val = Math.easeInOutQuad(currentTime, start, change, duration)
        // 执行滚动
        move(val)
        // 如果动画尚未完成，则继续执行动画
        if (currentTime < duration) {
            requestAnimFrame(animateScroll)
        } else {
            // 动画完成后调用回调函数（如果存在）
            if (callback && typeof (callback) === 'function') {
                callback()
            }
        }
    }

    // 启动动画
    animateScroll()
}
