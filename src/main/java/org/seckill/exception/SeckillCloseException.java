package org.seckill.exception;

/**
 * @program: seckill
 * @description: 秒杀关闭异常
 * @author: lm
 * @create: 2018-11-20 15:24
 **/
public class SeckillCloseException extends SeckillException {
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}