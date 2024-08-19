namespace Ytrue.Infrastructures.Security;

/// <summary>
///  接口按钮特性，映射到按钮权限
/// AttributeTargets.Method 表示这个特性只能应用到方法上。
/// AllowMultiple = false 表示该特性不能多次应用到同一个目标（方法）上。
/// Inherited = true 表示允许该特性在派生类中继承使用。
/// </summary>
[AttributeUsage(AttributeTargets.Method)]
public class PermissionAttribute : System.Attribute
{
    public PermissionAttribute(string permissionName)
    {
        PermissionName = permissionName;
    }

    public string PermissionName { get; private set; }
}