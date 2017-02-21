package com.forthe.xmemory.core;

public interface Const {
    int SVL_1 = 1;//安全等级1，des+固定key+shift
    int SVL_2 = 2;//安全等级2，des+固定key+shift+动态key
    int SVL_3 = 3;//安全等级3，des+固定key+shift+动态key+随机key+查询时密码

    int KEY_MIN_LEN = 8;
    int YET_POOLSIZE = 50;
    String SP_KEY = "xmemorie";
}
