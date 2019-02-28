package org.seckill.exception;

/**
 * @program: seckill
 * @description: 重复秒杀异常
 * @author: lm
 * @create: 2018-11-20 15:19
 **/
public class RepeatKillException extends SeckillException {
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}