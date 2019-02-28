package org.seckill.dao.itface;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.seckill.entity.SuccessKilled;

/**
 * @program: seckill
 * @description:
 * @author: lm
 * @create: 2018-11-16 16:21
 **/
@MapperScan
public interface ISuccessKilledDAO {
    /**
     * 插入购买明细，过滤重复
     * @param seckillId
     * @param userPhone
     * @return          插入行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据成功秒杀id查询购买明细并携带秒杀产品实例
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId")long seckillId, @Param("userPhone") long userPhone);
}