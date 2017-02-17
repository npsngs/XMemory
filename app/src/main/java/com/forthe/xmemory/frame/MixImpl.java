package com.forthe.xmemory.frame;

import android.text.TextUtils;

import com.forthe.xmemory.core.Mix;


public class MixImpl implements Mix {
    @Override
    public String mix(String... input) {
        if(null == input || input.length <= 0){
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        boolean isGoon = true;
        int count = 0;
        while (isGoon){
            boolean hasLenItem = false;
            for (String s:input){
                if(!TextUtils.isEmpty(s) && s.length()-1 > count){
                    hasLenItem = true;
                    sb.append(s.charAt(count));
                }
            }
            isGoon = hasLenItem;
            count++;
        }
        return sb.toString();
    }
}
