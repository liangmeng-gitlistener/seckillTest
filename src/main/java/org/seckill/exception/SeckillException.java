package org.seckill.exception;

/**
 * @program: seckill
 * @description:
 * @author: lm
 * @create: 2018-11-20 15:27
 **/
public class SeckillException extends RuntimeException {
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}