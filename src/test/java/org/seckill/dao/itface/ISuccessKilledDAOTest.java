package org.seckill.dao.itface;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @program: seckill
 * @description:
 * @author: lm
 * @create: 2018-11-20 10:27
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class ISuccessKilledDAOTest {

    @Resource
    private ISuccessKilledDAO iSuccessKilledDAO;

    @Test
    public void insertSuccessKilled() {
        long seckillId = 1004;
        long userPhone = 18362983376L;
        int insertCount = iSuccessKilledDAO.insertSuccessKilled(seckillId,userPhone);
        System.out.println("insertCount:" + insertCount);
    }

    @Test
    public void queryByIdWithSeckill() {
        long seckillId = 1004;
        long userPhone = 18362983376L;
        SuccessKilled successKilled = iSuccessKilledDAO.queryByIdWithSeckill(seckillId,userPhone);
        System.out.println("successKilled : " + successKilled);
        System.out.println(successKilled.getSeckill());
    }
}