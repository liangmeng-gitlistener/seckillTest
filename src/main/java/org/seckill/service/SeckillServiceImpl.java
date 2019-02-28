package org.seckill.service;

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
import java.util.List;

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
        Seckill seckill = getSeckillById(seckillId);
        if (seckill == null) {
            return new Exposer(false,seckillId);
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
            int updateCount = seckillDAO.reduceNumber(seckillId, now);
            if (updateCount <= 0){
                throw new SeckillCloseException("seckill is closed!");
            } else {
                int insertCount = successKilledDAO.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    throw new RepeatKillException("seckill repeated!");
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
}