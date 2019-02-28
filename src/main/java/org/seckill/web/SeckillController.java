package org.seckill.web;

import org.seckill.dto.*;
import org.seckill.entity.Seckill;
import org.seckill.enums.SysUtil;
import org.seckill.service.itface.ISeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
            produces = {"application.json;charset=UTF-8"})
    @ResponseBody//json申明
    public ResponseData<Exposer> exposer(Long seckillId){
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

    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public ResponseData<SeckillExcution> execute(String a){
        ResponseData<SeckillExcution> result;
        return result;
    }
}