package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.dao.cache.itface.IRedisDao;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

//public class RedisDaoImpl implements IRedisDao {
public class RedisDaoImpl {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;
    //  单位：秒
    private final Integer timeout;

    public RedisDaoImpl(String ip, int port, int timeout) {
        this.timeout = timeout;
        this.jedisPool = new JedisPool(ip, port);
    }
    public RedisDaoImpl(String ip, int port) {
        this.timeout = 60 * 60;//默认一小时
        this.jedisPool = new JedisPool(ip, port);
    }

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

//    @Override
    public Seckill getSeckill(long seckillId){
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckillId;
                //  redis没有实现内部序列化
                //  get -> byte[] -> 反序列化 -> Object
                //  https://github.com/eishay/jvm-serializers/wiki
                //  protostuff : pojo
                byte[] bytes = jedis.get(key.getBytes());
                if (bytes != null) {
                    //  空对象
                    Seckill seckill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                    return seckill;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return null;
    }

//    @Override
    public String putSeckill(Seckill seckill){
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                if(seckill != null){
                    String key = "seckill:" + seckill.getSeckillId();
                    byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
                            LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                    if (!bytesIsNull(bytes)) {
                        //  超时缓存
                        String result = jedis.setex(key.getBytes(), timeout, bytes);
                        return result;
                    }
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private static boolean bytesIsNull(byte[] bs){
        if(bs.length==0||bs==null){//根据byte数组长度为0判断
            return true;
        }
        return false;
    }
}
