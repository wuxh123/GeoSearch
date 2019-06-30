package cn.my.elasticsearch.service.impl;

import cn.my.elasticsearch.model.Person;
import cn.my.elasticsearch.repository.PersonRepository;
import cn.my.elasticsearch.service.PersonService;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxh on 2019/06/29.
 */
@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    private static final String PERSON_INDEX_NAME = "elastic_search_project";
    private static final String PERSON_INDEX_TYPE = "person";

    /**
     * Created By wuxh on 16:17 2019/06/29.
     * 插入测试数据
     */
    public Person add(Person person) {
        return personRepository.save(person);
    }

    /**
     * Created By wuxh on 16:17 2019/06/29.
     * 批量插入测试数据
     */
    public void bulkIndex(List<Person> personList) {
        int counter = 0;
        try {
            if (!elasticsearchTemplate.indexExists(PERSON_INDEX_NAME)) {
                elasticsearchTemplate.createIndex(PERSON_INDEX_TYPE);
            }
            List<IndexQuery> queries = new ArrayList<IndexQuery>();
            for (Person person : personList) {
                IndexQuery indexQuery = new IndexQuery();
                indexQuery.setId(person.getId() + "");
                indexQuery.setObject(person);
                indexQuery.setIndexName(PERSON_INDEX_NAME);
                indexQuery.setType(PERSON_INDEX_TYPE);

                //上面的那几步也可以使用IndexQueryBuilder来构建
                //IndexQuery index = new IndexQueryBuilder().withId(person.getId() + "").withObject(person).build();

                queries.add(indexQuery);
                if (counter % 500 == 0) {
                    elasticsearchTemplate.bulkIndex(queries);
                    queries.clear();
                    System.out.println("bulkIndex counter : " + counter);
                }
                counter++;
            }
            if (queries.size() > 0) {
                elasticsearchTemplate.bulkIndex(queries);
            }
            System.out.println("bulkIndex completed.");
        } catch (Exception e) {
            System.out.println("IndexerService.bulkIndex e;" + e.getMessage());
            try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }

    /**
     * 
     * 
     * @param id
     */
    public void delete(String id) {
    	elasticsearchTemplate.delete("id", "person", id);
    	System.out.println("delete success");
    }
    /**
     * Created By wuxh on 16:24 2019/06/29.
     * 查询 lat-纬度 lon-经度
     * geo_distance: 查找距离某个中心点距离在一定范围内的位置
     * geo_bounding_box: 查找某个长方形区域内的位置
     * geo_distance_range: 查找距离某个中心的距离在min和max之间的位置
     * geo_polygon: 查找位于多边形内的地点。
     * sort可以用来排序
     */
    public List<Person> query(Double lat, Double lon) {
        //查询某经纬度100米范围内
        GeoDistanceQueryBuilder builder = QueryBuilders.geoDistanceQuery("address").point(lat, lon).distance(10, DistanceUnit.METERS);

        GeoDistanceSortBuilder sortBuilder = SortBuilders.geoDistanceSort("address",lat, lon).unit(DistanceUnit.METERS).order(SortOrder.ASC);

        Pageable pageable = PageRequest.of(0,10000);

        NativeSearchQueryBuilder builder1 = new NativeSearchQueryBuilder().withFilter(builder).withSort(sortBuilder).withPageable(pageable);
        SearchQuery searchQuery = builder1.build();

        //queryForList默认是分页，走的是queryForPage，默认10个
        return elasticsearchTemplate.queryForList(searchQuery, Person.class);
    }
}
