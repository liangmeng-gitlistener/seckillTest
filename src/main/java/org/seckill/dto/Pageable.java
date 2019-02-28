package org.seckill.dto;

/**
 * @program: seckill
 * @description: 分页类, 提供当前页和每页记录数等参数
 * @author: lm
 * @create: 2018-11-20 15:45
 **/
public class Pageable {
//public class Pageable implements Comparable{
//public class Pageable implements Comparator<Pageable> {
    /**
     * 当前页数, 从 1 开始
     */
    private int pageNum = 1;
    /**
     * 每页记录数, 默认 10000
     */
    private int linage = 10000;
    /**
     * 默认没有排序字段
     */
    private String order = "";

    /**
     * 构造函数
     */
    public Pageable(){
        // 默认构造函数
    }

    /**
     * 构造函数
     * @param pageNum
     * @param linage
     */
    public Pageable(int pageNum, int linage) {
        this.pageNum = pageNum;
        this.linage = linage;
    }
    /**
     * 获取当前页
     * @return
     */
    public int getPageNum() {
        return pageNum;
    }
    /**
     * 设置当前页
     * @return
     */
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    /**
     * 获取每页记录数
     * @return
     */
    public int getLinage() {
        return linage;
    }
    /**
     * 设置每页记录数
     * @return
     */
    public void setLinage(int linage) {
        this.linage = linage;
    }
    /**
     * 获取排序字段
     * @return
     */
    public String getOrder() {
        return order;
    }

    /**
     * 设置排序字段
     * @return
     */
    public void setOrder(String order) {
        this.order = order;
    }
    /**
     * 重写equals
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if(o == null) {
            return false;
        }

        if (this.getClass() != o.getClass()) {
            return false;
        }

        Pageable pageable = (Pageable) o;

        if (getPageNum() != pageable.getPageNum()) {
            return false;
        }
        if (getLinage() != pageable.getLinage()) {
            return false;
        }
        return getOrder().equals(pageable.getOrder());

    }
    /**
     * 重写 hashCode
     * @return
     */
    @Override
    public int hashCode() {
        int result = getPageNum();
        result = 31 * result + getLinage();
        result = 31 * result + getOrder().hashCode();
        return result;
    }
    /**
     * 重写 toString
     * @return
     */
    @Override
    public String toString() {
        return "Pageable{" +
                "pageNum=" + pageNum +
                ", linage=" + linage +
                ", orderBy=" + order +
                '}';
    }
}