package com.sharing.common.utils;

import java.util.*;


/**
 * 类名称：SignAlgorithm.java
 * 类描述：签名算法
 * 创建时间：2015-5-29 上午10:53:10
 * @version V1.0
 */
public class SignAlgorithm {

	@SuppressWarnings("unused")
	private static String characterEncoding = "UTF-8";
	
	/**
	 * 生成签名
	 * @param sParaTemp
	 * @param key
	 * @param signType
	 * @param inputCharset
	 * @return String
	 */
	public static String createSign(Map<String, Object> sParaTemp,String key,String inputCharset){
		//除去数组中的空值和签名参数
        Map<String, Object> sPara = paraFilter(sParaTemp);
        return buildRequestMysign(sPara,key,inputCharset);
	}
	
    /**
     * 生成要请求给支付宝的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
	public static String buildXmlRequestPara(Map<String, Object> sParaTemp,String key,String inputCharset) {
        StringBuffer result = new StringBuffer();
        result.append("<xml>");
    	//除去数组中的空值和签名参数
        Map<String, Object> sPara = paraFilter(sParaTemp);
        //生成签名结果
        String mysign = createSignNoKey(sPara,key,inputCharset);

        List<String> keys = new ArrayList<String>(sParaTemp.keySet());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            String tempkey = keys.get(i);
            Object value = sParaTemp.get(tempkey);
            result.append("<"+tempkey+">"+value+"</"+tempkey+">");
        }
        result.append("<sign>"+mysign+"</sign>");
        result.append("</xml>");
        return result.toString();
    }
    
    /** 
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, Object> paraFilter(Map<String, Object> sArray) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            Object value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }
    
    /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, Object> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = params.get(key);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }
    
    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @param key	签名串
     * @param signType 签名方式
     * @param inputCharset 字符编码
     * @return 签名结果字符串
     */
	public static String buildRequestMysign(Map<String, Object> sPara, String key, String inputCharset) {
		String prestr = createLinkString(sPara); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		String mysign = "";
		try {
			mysign = RSA.sign(prestr, key, inputCharset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mysign;
	}
	
	public static boolean verify(Map<String, Object> sPara, String sign, String ali_public_key, String input_charset){
		String prestr = createLinkString(sPara);
		return RSA.verify(prestr, sign, ali_public_key, input_charset);
	}
	
	/**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @param key	签名串
     * @param signType 签名方式
     * @param inputCharset 字符编码
     * @return 签名结果字符串
     */
	public static String createSignNoKey(Map<String, Object> sPara,String key,String inputCharset) {
		sPara = paraFilter(sPara);
    	String prestr = createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        try {
    		mysign = RSAUtils.encryptByPublicKey(prestr.getBytes(), key).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return mysign;
    }
	
	/**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @param key	签名串
     * @param signType 签名方式
     * @param inputCharset 字符编码
     * @return 签名结果字符串
     */
	public static String createSignWithKey(Map<String, Object> sPara,String key,String inputCharset) {
    	String prestr = createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        try {
    		mysign = RSAUtils.encryptByPublicKey(prestr.getBytes(), key).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return mysign;
    }

	public static void main(String[] args) {
		String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJh6tsildqilIJOzK2VWoVYaZSp6EN8IEOIFAHkVmwAfldjpPY1LDCMe9lAkWoyYGhXY3hGTIqx9YFvRZA8RS2jiUE9t0ub2m3bQzG/YFlcBB7XVWma47BwHlhkwd6cTWVgWJzOU3wakKoeUARyBib4B8YpbhWqiNcRwuIvcYhgZAgMBAAECgYB/2r3zptLFfJjBYIYt1iM4WK+lm28UJOZLLxiDb3tl8hYrKQBEvr3xoR+2eZU3uL5+vTcQE4kKi6LFuuEkxUKt5tEtU15D7BaYPeZEsB1KFPs9aa/zCOX9g65dLC5AOeJEMjI0sY6mmYy7+tyQCAWNHs048uFLRAeDzwGGLHEmVQJBANG/5q3ksj8HLy2e3a7QfTCASYLoWBaJ1+hpko0H1IEnBobph6uD1wml9H6irDIAngHV3y5VrUqsvflqSD1kiWcCQQC6Gfq9oY7Og/DbKDrCxXrFIftcySivfPi01/g+TYneluVTagtk0Jp27zqiSDo0LsbhlEPdKBItRi0YxfpmA+J/AkEAnN1s8r4nxFVmJjM6zYOlLMG0uI91WZ3LeEiETq79cX2zNvCuA0q9ZrzThmW4bEjj4rGFkQyIZmGcTkz7K9blnwJAeaIVQe/f+JY/y92lWjvcHBAzy9cmIlyZ6QjnFUF0hOk3BaffvoqhCm8dcYenkwensTRIjrlr9+9P9ksfd6G+XQJBAMK2+p4K/UBdz7lGcll7Hh7xaXi2cVoN6MdAKqikA2v998SGEBLvWAEKuiKJFiLdm3sKBrQOjXgJWZi520xCzC0=";
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYerbIpXaopSCTsytlVqFWGmUqehDfCBDiBQB5FZsAH5XY6T2NSwwjHvZQJFqMmBoV2N4RkyKsfWBb0WQPEUto4lBPbdLm9pt20Mxv2BZXAQe11VpmuOwcB5YZMHenE1lYFiczlN8GpCqHlAEcgYm+AfGKW4VqojXEcLiL3GIYGQIDAQAB";
		Map<String, Object> bizContent = new HashMap<String, Object>();
		bizContent.put("request_id", "1566799555413");
		bizContent.put("store_id", "9356");
		bizContent.put("out_trade_no", "15667995489443951");
		bizContent.put("refund_fee", 10.00);
		bizContent.put("refund_reason", "不买了");
		String sign = SignAlgorithm.buildRequestMysign(bizContent, privateKey, "utf-8");
		System.out.println(sign);
		boolean verify = SignAlgorithm.verify(bizContent, sign, publicKey, "utf-8");
		System.out.println(verify);
	}
}
