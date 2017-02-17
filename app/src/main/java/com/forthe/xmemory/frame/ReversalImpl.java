package com.forthe.xmemory.frame;

import android.text.TextUtils;

import com.forthe.xmemory.core.Reversal;


public class ReversalImpl implements Reversal {
    @Override
    public String reversal(String input) {
        if(TextUtils.isEmpty(input)){
            return input;
        }
        StringBuilder sb = new StringBuilder();
        for(int i = input.length()-1;i>=0;i--){
            sb.append(input.charAt(i));
        }
        return sb.toString();
    }
}
