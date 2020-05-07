package com.sharing.common.utils;

import java.util.Random;
import java.util.UUID;

/**
 *
 * 类名称: HashCodeUtils
 * 类描述: TODO
 * 创建人: 徐
 * 创建时间: 2018年5月11日 下午6:18:41
 * 修改备注:
 * @version1.0
 */
public final class HashCodeUtils {
    private HashCodeUtils() {
    }

    /**
     * HashCode
     * @return HashCode
     */
    public static String getHashCode() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid;
    }

    static String[] chars = new String[]{
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
        "k", "m", "n", "p", "q", "r", "s", "t",
        "u", "v", "w", "x", "y", "z"};

    /**
     * 生成模板元素id
     * @param pre
     * @param idlength
     * @return
     */
    public static String getItemId(String pre, int idlength){
        if(pre==null||idlength<0) return null;
        String id = pre;
        int prelen = pre.length();
        Random r = new Random();
        for (int i = 0; i < idlength-prelen; i++) {
            int pos = r.nextInt(chars.length);
            id += chars[pos];
        }
        return id;
    }

}
