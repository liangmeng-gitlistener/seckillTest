package org.seckill.dao.itface;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

/**
 * @program: seckill
 * @description:
 * @author: lm
 * @create: 2018-11-16 09:29
 **/
@MapperScan
public interface ISeckillDAO {
    /**
     * 根据起始与停止位置全量查询秒杀商品列表
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> querryAll(@Param("offset") int offset, @Param("limit") int limit);
    /**
     * 根据秒杀id查询秒杀对象
     * @param seckillId
     * @return
     */
    Seckill querryById(long seckillId);
    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return          更新记录行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);
}