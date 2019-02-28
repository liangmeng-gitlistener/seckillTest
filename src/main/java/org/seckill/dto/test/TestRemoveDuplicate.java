package org.seckill.dto.test;

import org.seckill.dto.Pageable;

import java.util.*;

/**
 * @program: seckill
 * @description:
 * @author: lm
 * @create: 2019-02-23 17:28
 **/
public class TestRemoveDuplicate {

    public static ArrayList removeDuplicate(List ls){
        Set<Pageable> ts = new TreeSet<Pageable>(new Comparator<Pageable>(){
            @Override
            public int compare(Pageable o1, Pageable o2) {
                if(o1.getPageNum() == o2.getPageNum()){
                   return Integer.valueOf(o1.getLinage()).compareTo(Integer.valueOf(o2.getLinage()));
                }
                return Integer.valueOf(o1.getPageNum()).compareTo(Integer.valueOf(o2.getPageNum()));//升序
            }
        });
        ts.addAll(ls);
        return new ArrayList<Pageable>(ts);
    }
}