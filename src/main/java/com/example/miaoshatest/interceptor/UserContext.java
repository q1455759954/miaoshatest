package com.example.miaoshatest.interceptor;

import com.example.miaoshatest.dao.bean.MiaoShaUser;

public class UserContext {

    private static ThreadLocal<MiaoShaUser> threadLocal = new ThreadLocal<MiaoShaUser>();

    public static void setUser(MiaoShaUser user) {
        threadLocal.set(user);
    }

    public static MiaoShaUser getUser() {
        return threadLocal.get();
    }

    public static void removeUser() {
        threadLocal.remove();
    }
}
