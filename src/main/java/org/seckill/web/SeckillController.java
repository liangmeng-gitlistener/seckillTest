package org.seckill.web;

import org.seckill.dto.*;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.enums.SysUtil;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.itface.ISeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @program: seckill
 * @description:
 * @author: lm
 * @create: 2018-11-26 14:20
 **/
@Controller
@RequestMapping(SysUtil.API + "/seckill")
public class SeckillController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private String test = SysUtil.API_BASE.toString();

    @Autowired
    private ISeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model){
        Pageable pageable = new Pageable(1, 100);
        List<Seckill> list = seckillService.getSeckillList(pageable);
        model.addAttribute("list", list);
        //list.jsp + model = ModelAndView
        return "list"; // WEB-INF/jsp/list.jsp
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId")Long seckillId, Model model){
        if (seckillId == null) {
            return "redirect:" + SysUtil.API + "/seckill" + "/list";
        }
        Seckill seckill = seckillService.getSeckillById(seckillId);
        if (seckill == null) {
            return "forward:" + SysUtil.API + "/seckill" + "/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }

    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody//json申明
    public ResponseData<Exposer> exposer(@PathVariable("seckillId") Long seckillId){
        ResponseData<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillURL(seckillId);
//            result = new ResponseSuccess<Exposer>("开启秒杀成功",exposer);
            result = new ResponseSuccess<Exposer>();
            result.setInfo("开启秒杀成功!");
            result.setResult(exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new ResponseError<Exposer>(e.getMessage());
        }
        return result;
    }

    /**
     * 执行秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public ResponseData<SeckillExcution> execute(@PathVariable("seckillId") Long seckillId,
                                                 @CookieValue(value = "killPhone", required = false) Long userPhone,
                                                 @PathVariable("md5") String md5) {
        if (userPhone == null) {
            return new ResponseError<SeckillExcution>("未注册");
        }
        ResponseData<SeckillExcution> result;
        try {
            SeckillExcution excution = seckillService.excuteSeckill(seckillId, userPhone, md5);
            return new ResponseSuccess<SeckillExcution>("执行" + SeckillStateEnum.SUCCESS.getStateInfo(), excution);
        } catch (RepeatKillException e) {
            SeckillExcution excution = new SeckillExcution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new ResponseSuccess<SeckillExcution>("重复秒杀",excution);
        } catch (SeckillCloseException e){
            SeckillExcution excution = new SeckillExcution(seckillId, SeckillStateEnum.END);
            return new ResponseSuccess<SeckillExcution>("秒杀关闭",excution);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        SeckillExcution excution = new SeckillExcution(seckillId, SeckillStateEnum.INNER_ERROR);
        return new ResponseSuccess<SeckillExcution>("未知错误",excution);
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public  ResponseData<Long> time(){
        Date now = new Date();
        return new ResponseSuccess<Long>("获取当前时间成功！", now.getTime());
    }
}