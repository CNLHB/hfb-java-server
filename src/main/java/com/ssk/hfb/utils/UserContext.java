package com.ssk.hfb.utils;

import com.ssk.hfb.common.pojo.UserInfo;

/**
 * 使用线程上下文在线程内共享用户信息
 */
public class UserContext {

    //把构造函数私有化，外部不能new
    private UserContext() {
    }

    private static final ThreadLocal<UserInfo> context = new ThreadLocal<UserInfo>();

    /**
     * 存放用户信息
     *
     * @param user
     */
    public static void set(UserInfo user) {
        context.set(user);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static UserInfo get() {
        return context.get();
    }
    /**
     * 获取用户信息
     *
     * @return
     */
    public static int getId() {
        if (context.get() == null){
            return -1;
        }
        return context.get().getUserId();
    }
    /**
     * 清除当前线程内引用，防止内存泄漏
     */
    public static void remove() {
        context.remove();
    }
}
