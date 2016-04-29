package edu.bupt.sia.ccn.services;

/**
 * Created by yangkuang on 16-4-29.
 */
public class CountTable implements Comparable<CountTable>{
    private String key;
    private Integer count;
    public CountTable (String key, Integer count) {
        this.key = key;
        this.count = count;
    }
    public int compareTo(CountTable o) {
        int cmp = count.intValue() - o.count.intValue();
        return (cmp == 0 ? key.compareTo(o.key) : -cmp);
        //-cmp降序排列 cmp升序排列
        //TreeSet会调用WorkForMap的compareTo方法来决定自己的排序
    }

    @Override
    public String toString() {

        return key + " 出现的次数为：" + count;
    }

    public String getKey() {

        return key;
    }

    public Integer getCount() {

        return count;
    }
}
