package com.sharing.common.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    /**
     * md5加密 32位
     *
     * @param str
     * @return
     */
    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] byteDigest = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            putValueToBuffer(byteDigest, buf);
            // 32位加密
            return buf.toString();
            // 16位的加密
            // return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void putValueToBuffer(byte[] byteDigest, StringBuffer buf) {
        int i;
        for (int offset = 0; offset < byteDigest.length; offset++) {
            i = byteDigest[offset];
            if (i < 0) {
                i += 256;
            }
            if (i < 16) {
                buf.append("0");
            }
            buf.append(Integer.toHexString(i));
        }
    }

    /**
     * md5加密 16位
     *
     * @param str
     * @return
     */
    public static String md5_16(String str) throws UnsupportedEncodingException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] byteDigest = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            putValueToBuffer(byteDigest, buf);
            // 32位加密
            // return buf.toString();
            // 16位的加密
            return buf.toString().substring(8, 24).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String md5Encode_32(String inStr) {
        MessageDigest md5 = null;
        StringBuffer hexValue = new StringBuffer();
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] byteArray = inStr.getBytes("UTF-8");
            byte[] md5Bytes = md5.digest(byteArray);
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = (md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return hexValue.toString();
    }

}
