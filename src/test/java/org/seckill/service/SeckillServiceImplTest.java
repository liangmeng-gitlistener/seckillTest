package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.Pageable;
import org.seckill.dto.SeckillExcution;
import org.seckill.entity.Seckill;
import org.seckill.enums.SysUtil;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.itface.ISeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @program: seckill
 * @description:
 * @author: lm
 * @create: 2018-11-23 17:04
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml",
        "classpath:spring/spring-utils.xml"})
public class SeckillServiceImplTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISeckillService seckillService;

    @Test
    public void getSeckillList() {
        Pageable pageable = new Pageable(1,3);
        List<Seckill> seckillList = seckillService.getSeckillList(pageable);
        logger.info("list = {}",seckillList);
    }

    @Test
    public void getSeckillById() {
        long id = 1004;
        Seckill seckill = seckillService.getSeckillById(id);
        logger.info("seckill = {}",seckill);
    }

    @Test
    public void exportSeckillURL() {
        long id = 1000;
        Exposer exposer = seckillService.exportSeckillURL(id);
        logger.info("expose = {}",exposer);
    }

    @Test
    public void excuteSeckill() {
        long id = 1004;
        long phone = 18598762548L;
        String md5 = "25884f24e6c740978f73938e8ea95283";
        try {
            SeckillExcution seckillExcution = seckillService.excuteSeckill(id, phone, md5);
            logger.info("SeckillExcution = {}", seckillExcution);
        } catch (SeckillCloseException e) {
            logger.info(e.getMessage());
        } catch (RepeatKillException e) {
            logger.info(e.getMessage());
        }
    }

    @Test
    public void excute(){
        long id = 1005;
        Exposer exposer = seckillService.exportSeckillURL(id);
        if (exposer.isExposed()){
            logger.info("expose = {}",exposer);
            long phone = 18598762548L;
            String md5 = exposer.getMd5();
            try {
                SeckillExcution seckillExcution = seckillService.excuteSeckill(id, phone, md5);
                logger.info("SeckillExcution = {}", seckillExcution);
            } catch (SeckillCloseException e) {
                logger.info(e.getMessage());
            } catch (RepeatKillException e) {
                logger.info(e.getMessage());
            }
        } else {
            logger.warn("expose = {}",exposer);
        }
    }
}