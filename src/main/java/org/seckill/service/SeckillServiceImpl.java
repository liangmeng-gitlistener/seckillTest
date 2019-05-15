package org.seckill.service;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.cache.RedisDaoImpl;
import org.seckill.dao.itface.ISeckillDAO;
import org.seckill.dao.itface.ISuccessKilledDAO;
import org.seckill.dto.Exposer;
import org.seckill.dto.Pageable;
import org.seckill.dto.SeckillExcution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.itface.ISeckillService;
import org.seckill.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: seckill
 * @description:
 * @author: lm
 * @create: 2018-11-20 15:33
 **/
// @Component @Controller @Service
@Service
public class SeckillServiceImpl implements ISeckillService {
    private Logger log = LoggerFactory.getLogger(this.getClass());
//    @Resource @Inject
    @Autowired
    private ISeckillDAO seckillDAO;
    @Autowired
    private ISuccessKilledDAO successKilledDAO;
    @Autowired
    private MD5Util md5Util;
    @Autowired
    private RedisDaoImpl redisDao;

    @Override
    public List<Seckill> getSeckillList(Pageable pageable) {
        int offset = (pageable.getPageNum() - 1) * pageable.getLinage();
        int limit = pageable.getLinage();
        return seckillDAO.querryAll(offset,limit);
    }

    @Override
    public Seckill getSeckillById(long seckillId) {
        return seckillDAO.querryById(seckillId);
    }

    @Override
    public Exposer exportSeckillURL(long seckillId) {
        //  缓存优化，一致性建立在超时基础上
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            seckill = getSeckillById(seckillId);
            if (seckill == null) {
                return new Exposer(false,seckillId);
            } else {
                redisDao.putSeckill(seckill);
            }
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date now = new Date();
        if (now.getTime() < startTime.getTime()
                || now.getTime() > endTime.getTime()){
            return new Exposer(false, seckillId,
                    now.getTime(), startTime.getTime(), endTime.getTime());
        }
        String md5 = md5Util.getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    @Override
    @Transactional
    public SeckillExcution excuteSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(md5Util.getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        Date now = new Date();
        try {
            //  先更新，后插入行锁时间更久
//            int updateCount = seckillDAO.reduceNumber(seckillId, now);
//            if (updateCount <= 0){
//                throw new SeckillCloseException("seckill is closed!");
//            } else {
//                int insertCount = successKilledDAO.insertSuccessKilled(seckillId, userPhone);
//                if (insertCount <= 0) {
//                    throw new RepeatKillException("seckill repeated!");
//                } else {
//                    SuccessKilled successKilled = successKilledDAO.queryByIdWithSeckill(seckillId, userPhone);
//                    return new SeckillExcution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
//                }
//            }
            //  先插入，再更新效率高
            int insertCount = successKilledDAO.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                //  防重秒
                throw new RepeatKillException("seckill repeated!");
            } else {
                int updateCount = seckillDAO.reduceNumber(seckillId, now);
                if (updateCount <= 0){
                    throw new SeckillCloseException("seckill is closed!");
                } else {
                    SuccessKilled successKilled = successKilledDAO.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExcution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e){
            log.error(e.getMessage(), e);
            // 所有编译期异常转化为运行期异常
            throw new SeckillException(e.getMessage(), e);
        }
    }

    @Override
    public SeckillExcution excuteSeckillProcedure (long seckillId, long userPhone, String md5) {
        //  TODO
        if (md5 == null || !md5.equals(md5Util.getMD5(seckillId))){
            return new SeckillExcution(seckillId, SeckillStateEnum.DATA_REWRITE);
        }
        Date killTime = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);
        try {
            seckillDAO.killByProcedure(map);
            int result = MapUtils.getInteger(map, "result", -2);
            if (result == 1) {
                SuccessKilled sk =
                        successKilledDAO.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExcution(seckillId, SeckillStateEnum.SUCCESS, sk);
            } else {
                return new SeckillExcution(seckillId, SeckillStateEnum.stateof(result));
            }
        } catch (Exception e) {
//            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return new SeckillExcution(seckillId, SeckillStateEnum.INNER_ERROR);
    }
}