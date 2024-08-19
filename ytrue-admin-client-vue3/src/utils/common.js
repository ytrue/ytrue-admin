/**
 * 参数处理
 *  假设传入的参数对象 params 如下：
 *  const params = {
 *   name: 'John Smith',
 *   age: 30,
 *   address: {
 *     city: 'New York',
 *     postalCode: '10001'
 *   },
 *   hobbies: ['reading', 'traveling'],
 *   isActive: true,
 *   emptyField: null,
 *   undefinedField: undefined
 * }
 *
 * 将参数对象转换为 URL 查询字符串
 * @param {Object} params - 要转换的参数对象
 * @returns {string} 转换后的查询字符串
 */
/**
 * 参数处理
 * @param {*} params  参数
 */
export function tansParams(params) {
    let result = ''
    for (const propName of Object.keys(params)) {
        const value = params[propName];
        const part = encodeURIComponent(propName) + "=";
        if (value !== null && value !== "" && typeof (value) !== "undefined") {
            if (typeof value === 'object') {
                for (const key of Object.keys(value)) {
                    if (value[key] !== null && value[key] !== "" && typeof (value[key]) !== 'undefined') {
                        let params = propName + '[' + key + ']';
                        const subPart = encodeURIComponent(params) + "=";
                        result += subPart + encodeURIComponent(value[key]) + "&";
                    }
                }
            } else {
                result += part + encodeURIComponent(value) + "&";
            }
        }
    }
    return result
}


/**
 * 返回项目路径
 * @param p
 * @returns {*}
 */
export function getNormalPath(p) {
    if (p.length === 0 || !p || p === 'undefined' || p === undefined) {
        return p
    }
    let res = p.replace('//', '/')
    if (res[res.length - 1] === '/') {
        return res.slice(0, res.length - 1)
    }
    return res
}



/**
 * 树形数据转换
 * @param {*} data
 * @param {*} id
 * @param {*} pid
 * @param children
 */
export function treeDataTranslate(data, id = 'id', pid = 'pid', children = 'children') {
    let res = []
    let temp = {}
    for (let i = 0; i < data.length; i++) {
        temp[data[i][id]] = data[i]
    }
    for (let k = 0; k < data.length; k++) {
        if (temp[data[k][pid]] && data[k][id] !== data[k][pid]) {
            if (!temp[data[k][pid]][children]) {
                temp[data[k][pid]][children] = []
            }
            if (!temp[data[k][pid]]['_level']) {
                temp[data[k][pid]]['_level'] = 1
            }
            data[k]['_level'] = temp[data[k][pid]]._level + 1
            temp[data[k][pid]][children].push(data[k])
        } else {
            res.push(data[k])
        }
    }
    return res
}
