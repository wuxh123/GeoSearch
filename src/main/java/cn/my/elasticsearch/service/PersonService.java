package cn.my.elasticsearch.service;

import java.util.List;

import cn.my.elasticsearch.model.Person;

/**
 * Created by wuxh on 2019/06/29.
 */
public interface PersonService {
    /**
     * Created By wuxh on 16:14 2019/06/29.
     * 插入测试数据
     */
    Person add(Person person);
    void delete(String id);
    /**
     * Created By wuxh on 16:14 2019/06/29.
     * 批量查询测试数据
     */
    void bulkIndex(List<Person> personList);
    /**
     * Created By wuxh on 16:23 2019/06/29.
     * 查询 lat-纬度 lon-经度
     */
    List<Person> query(Double lat, Double lon);
}
