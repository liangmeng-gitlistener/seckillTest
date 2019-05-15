package org.seckill.service.itface;

import org.seckill.dto.Exposer;
import org.seckill.dto.Pageable;
import org.seckill.dto.SeckillExcution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * @program: seckill
 * @description:
 * @author: lm
 * @create: 2018-11-20 14:28
 **/
public interface ISeckillService {
    /**
     * 获取全量的秒杀记录
     * @return
     */
    List<Seckill> getSeckillList(Pageable pageable);

    /**
     * 根据秒杀货物ID获取秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getSeckillById(long seckillId);

    /**
     * 秒杀开启时返回秒杀接口地址
     * 否则输出系统时间和秒杀开启时间
     * @param seckillId
     */
    Exposer exportSeckillURL (long seckillId);

    /**
     * 执行秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillCloseException
     */
    SeckillExcution excuteSeckill (long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException;

    /**
     * 执行秒杀  by 存储过程
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillCloseException
     */
    SeckillExcution excuteSeckillProcedure (long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException;
}