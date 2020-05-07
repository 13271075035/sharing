package com.sharing.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sharing.common.exception.DIYException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ASUS on 2018/11/19.
 */
@SuppressWarnings("restriction")
@Component
public class HttpClientUtils {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    public static String sendRequest(String urlString,String paramStr,String method) throws DIYException {
        long start = System.currentTimeMillis();
        URL url = null;
        HttpURLConnection conn = null;
        String sTotalString = "";
        try {
            if (method.equals("GET")) {
                url = new URL(urlString+paramStr);
            } else {
                url = new URL(urlString);
            }
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestMethod(method);// 提交模式
            conn.setConnectTimeout(60*1000);
            conn.setReadTimeout(60*1000);
            conn.setDoOutput(false);// 是否输入参数
            if (method.equals("POST")) {
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                byte[] bypes = paramStr.getBytes();
                conn.getOutputStream().write(bypes);// 输入参数
            }
            InputStream inStream = null;
            if (conn.getResponseCode() >= 400 ) {
                inStream = conn.getErrorStream();
            } else {
                inStream = conn.getInputStream();
            }
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inStream, "UTF-8"));
            String sCurrentLine = "";
            while ((sCurrentLine = bufferedReader.readLine()) != null) {
                sTotalString += sCurrentLine;
            }
            String code = "10034";
            String msg = "调用外部接口错误";
            if (JSON.isValid(sTotalString)) {
                JSONObject parse = JSON.parseObject(sTotalString);
                code = parse.getString("code");
                msg = parse.getString("msg");
            }
            long end = System.currentTimeMillis();
            long time = end - start;
            logger.info("|mart|httpClient|"+urlString+"|"+code+"|"+time +"|null|"+msg+"|"+paramStr);
            return sTotalString;
        } catch (Exception e) {
            long end = System.currentTimeMillis();
            long time = end - start;
            logger.error("|mart|httpClient|"+urlString+"|10034|"+time +"|null|"+e.getMessage()+"|"+paramStr);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return sTotalString;
    }

}
