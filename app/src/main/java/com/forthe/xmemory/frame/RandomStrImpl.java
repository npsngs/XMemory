package com.forthe.xmemory.frame;
import com.forthe.xmemory.core.RandomStr;

public class RandomStrImpl implements RandomStr {
    private static final char[] charTable = {
            '1','2','3','4','5','6','7','8','0','_',
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
    };
    @Override
    public String createRandomStr(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<len;i++){
            sb.append(charTable[(int) (Math.random()*charTable.length)]);
        }
        return sb.toString();
    }

    @Override
    public String createRandomStr(int maxLen, int minlen) {
        int len = (int) (minlen + Math.random()*(maxLen-minlen));
        return createRandomStr(len);
    }
}
