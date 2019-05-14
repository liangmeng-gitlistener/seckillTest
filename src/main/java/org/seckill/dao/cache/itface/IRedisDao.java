package org.seckill.dao.cache.itface;

import org.seckill.entity.Seckill;

public interface IRedisDao {
    public Seckill getSeckill(long seckillId);
    public String putSeckill(Seckill seckill);
}
