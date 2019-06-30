package cn.my.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import cn.my.elasticsearch.model.Person;

/**
 * Created by wuxh on 2019/06/29.
 */
public interface PersonRepository extends ElasticsearchRepository<Person,Integer> {

}
