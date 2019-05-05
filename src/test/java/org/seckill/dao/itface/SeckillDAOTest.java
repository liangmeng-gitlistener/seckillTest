package org.seckill.dao.itface;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @program: seckill
 * @description:
 * @author: lm
 * @create: 2018-11-19 17:14
 **/
// junit启动时加载springIOC容器
@RunWith(SpringJUnit4ClassRunner.class)
// 注解告知spring配置文件目录
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SeckillDAOTest {
    // 注入dao实现依赖
    @Resource
    private ISeckillDAO seckillDAO;

    @Test
    public void querryById() {
        long seckillId = 1003;
        Seckill seckill = seckillDAO.querryById(seckillId);
        System.out.println(seckill);
    }

    @Test
    public void querryAll() {
        List<Seckill> seckills = seckillDAO.querryAll(0,100);
        for (Seckill seckill: seckills) {
            System.out.println(seckill);
        }
    }

    @Test
    public void reduceNumber() {
        Date now = new Date();
        int updateCount = seckillDAO.reduceNumber(1004, now);
        System.out.println("updateCount = " + updateCount);
    }
}