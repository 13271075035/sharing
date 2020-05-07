package com.sharing.common.utils;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class JedisUtil {
    private static String KEY = "key";
    private static String DATE = "date";
    /**
     * NO
     */
    public static final String DICT_GLOBAL_STR_NO = "NO";
    private static final String ERROR_TITLE = "set redis error,result =";

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    JedisPool jedisPool;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 获取Redis实例.
     * @return Redis工具类实例
     */
    public Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (Exception e) {
            log.error("Redis connection timeout！");
        }
        return jedis;
    }

    public String get(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.get(key);
        } finally {
            jedis.close();
        }
    }

    public Map<String, String> hgetAll(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.hgetAll(key);
        } finally {
            jedis.close();
        }
    }

    /**
     * 根据key获得唯一id(带前缀)
     * @param key 前缀
     * @return
     */
    public String getSeqByPrefix(String key, int zerofillLen) {
        return String.format("%s%0" + zerofillLen + "d", key, this.getNextSeq(key, true, 4000));
    }

    /**
     * 根据key获得唯一id(数据库insert专用)
     * @param map
     * @return
     */
    public long getNextId(final Map<String, Object> map) {
        Jedis jedis = getJedis();
        try {
            if (jedis != null) {
                String key = (String) map.get(KEY);
                long id = jedis.incr(key);
                long nextId = Long.parseLong(String.format("%d%d", map.get(DATE), id));
                if (id == 1L) {
                    jedis.expire(key, 100000);
                }
                return nextId;
            }
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0L;
    }

    /**
     * 根据key获得唯一序列
     * @param key
     * @param expire 是否设置key有效期
     * @param seconds 有效期 秒
     * @return
     */
    public long getNextSeq(final String key, Boolean expire, int seconds) {
        Jedis jedis = getJedis();
        try {
            if (jedis != null) {
                long id = jedis.incr(key);
                if (expire && id == 1L) {
                    jedis.expire(key, seconds);
                }
                return id;
            }
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0L;
    }

    /**
     * 获取字符串并释放redis实例到连接池
     * @param key
     * @return
     */
    public String getString(String key) {
        String value = StringUtils.EMPTY;
        Jedis jedis = getJedis();
        try {
            if (jedis != null) {
                value = jedis.get(key);
            }
            return value;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    /**
     * 设置字符串并释放redis实例到连接池
     * @param key
     * @return
     */
    public String setString(String key, String value) {
        Jedis jedis = getJedis();
        String result = DICT_GLOBAL_STR_NO;
        try {
            if (jedis != null) {
                result = jedis.set(key, value);
            } else {
                log.error(ERROR_TITLE + result);
            }
            return result;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 设置有过期时间的字符串并释放redis实例到连接池
     * @param key
     * @return
     */
    public String setExString(String key, int seconds, String value) {
        Jedis jedis = getJedis();
        String result = DICT_GLOBAL_STR_NO;
        try {
            if (jedis != null) {
                result = jedis.setex(key, seconds, value);
            } else {
                log.error(ERROR_TITLE + result);
            }
            return result;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     *
     * @param key
     * @param field
     * @return
     */
    public String hget(String key, String field) {
        Jedis jedis = getJedis();
        String value = StringUtils.EMPTY;
        try {
            if (jedis != null) {
                value = jedis.hget(key, field);
            }
            return value;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    /**
     *
     * @param key
     * @param hash
     * @return
     */
    public String hmset(String key, Map<String, String> hash) {
        Jedis jedis = getJedis();
        String result = DICT_GLOBAL_STR_NO;
        try {
            if (jedis != null) {
                result = jedis.hmset(key, hash);
            } else {
                log.error(ERROR_TITLE + result);
            }
            return result;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String hmgetByMapKey(String key, String field) {
        Jedis jedis = getJedis();
        List<String> resultList = null;
        String result = StringUtils.EMPTY;

        try {
            if (jedis != null) {
                resultList = jedis.hmget(key, field);
                result = CollectionUtils.isNotEmpty(resultList) ? resultList.get(0) : StringUtils.EMPTY;
            } else {
                log.error(ERROR_TITLE + result);
            }
            return result;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long expire(String key, int seconds) {
        Jedis jedis = getJedis();
        try {
            if (jedis != null) {
                return jedis.expire(key, seconds);
            } else {
                log.error("set redis expire error");
            }
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 根据key删除
     * @param key
     * @return
     * @author icedmazes
     */
    public long del(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.del(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long incr(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.incr(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String setExByte(byte[] key, int seconds, byte[] value) {
        Jedis jedis = getJedis();
        String result = DICT_GLOBAL_STR_NO;
        try {
            if (jedis != null) {
                Pipeline p = jedis.pipelined();
                Response<String> re = p.setex(key, seconds, value);
                p.sync();
                p.close();
                result = re.get();
            } else {
                log.error(ERROR_TITLE + result);
            }
            return result;
        } catch(Exception e){
            log.error("getByte redis error ->",e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    public byte[] getByte(byte[] key) {
        Jedis jedis = getJedis();
        byte[] result = new byte[0];
        try {
            if (jedis != null) {
                Pipeline p = jedis.pipelined();
                Response<byte[]> re = p.get(key);
                p.sync();
                p.close();
                result = re.get();
            } else {
                if (null != result){
                    log.error(ERROR_TITLE + Arrays.toString(result));
                }
            }
            return result;
        } catch(Exception e){
            log.error("getByte redis error ->",e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }
}
