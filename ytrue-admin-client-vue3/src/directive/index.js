import hasRole from './permission/hasRole'
import hasPermission from './permission/hasPermission'

export default function installPlugins(app) {
    app.directive('hasRole', hasRole)
    app.directive('hasPermission', hasPermission)
}
