package com.ytrue.infra.core.util;

public class ClassUtil {
    /**
     * 判断指定类是否存在
     *
     * @param className 要检查的类名
     * @param classLoader 类加载器
     * @return 类是否存在
     */
    public static boolean isClassPresent(String className, ClassLoader classLoader) {
        try {
            Class.forName(className, false, classLoader);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 判断指定对象的类是否是给定基类的子类
     *
     * @param obj 对象实例
     * @param baseClass 基类
     * @return 是否是基类的子类
     */
    public static boolean isSubclass(Object obj, Class<?> baseClass) {
        if (obj == null) {
            return false;
        }
        return baseClass.isAssignableFrom(obj.getClass());
    }

    /**
     * 判断指定类是否是给定基类的子类
     *
     * @param className 要检查的类名
     * @param baseClass 基类
     * @param classLoader 类加载器
     * @return 是否是基类的子类
     */
    public static boolean isSubclass(String className, Class<?> baseClass, ClassLoader classLoader) {
        try {
            Class<?> clazz = Class.forName(className, false, classLoader);
            return baseClass.isAssignableFrom(clazz);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


    /**
     * 判断指定类是否存在，并且是否是当前类或其子类
     *
     * @param className 要检查的类名
     * @param currentClass 当前类
     * @param classLoader 类加载器
     * @return 类是否存在且是当前类或其子类
     */
    public static boolean isClassPresentAndSubclass(String className, Class<?> currentClass, ClassLoader classLoader) {
        try {
            Class<?> clazz = Class.forName(className, false, classLoader);
            return clazz.isAssignableFrom(currentClass);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


    /**
     * 判断指定类是否实现了某个接口
     *
     * @param className 要检查的类名
     * @param interfaceClass 接口类
     * @param classLoader 类加载器
     * @return 是否实现了接口
     */
    public static boolean implementsInterface(String className, Class<?> interfaceClass, ClassLoader classLoader) {
        try {
            Class<?> clazz = Class.forName(className, false, classLoader);
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> iFace : interfaces) {
                if (interfaceClass.isAssignableFrom(iFace)) {
                    return true;
                }
            }
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
