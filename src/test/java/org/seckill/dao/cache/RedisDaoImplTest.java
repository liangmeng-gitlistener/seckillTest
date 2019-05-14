package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.itface.ISeckillDAO;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

// junit启动时加载springIOC容器
@RunWith(SpringJUnit4ClassRunner.class)
// 注解告知spring配置文件目录
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class RedisDaoImplTest {
    private long id = 1000;

    @Autowired
    private RedisDaoImpl redisDao;
    @Autowired
    private ISeckillDAO seckillDAO;
    @Test
    public void testSeckill() throws Exception{

        Seckill seckill = redisDao.getSeckill(id);
        if (seckill == null) {
            seckill = seckillDAO.querryById(id);
            String result = redisDao.putSeckill(seckill);
            System.out.println(result);
            seckill = redisDao.getSeckill(id);
            System.out.println(seckill);
        }
    }
}