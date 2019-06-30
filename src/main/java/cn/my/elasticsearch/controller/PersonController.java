package cn.my.elasticsearch.controller;

import cn.my.elasticsearch.model.Person;
import cn.my.elasticsearch.service.PersonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wuxh on 2019/06/29.
 */
//@RestController注解相当于@ResponseBody ＋ @Controller合在一起的作用。
@RestController
public class PersonController {
    @Autowired
    private PersonService personService;

    /**
     * Created By wuxh on 16:31 2019/06/29.
     * 批量添加测试数据
     * Spring4.3中引进了｛@GetMapping、@PostMapping、@PutMapping、@DeleteMapping、@PatchMapping｝@GetMapping是一个组合注解，
     * 是@RequestMapping(method = RequestMethod.GET)的缩写
     */
    @GetMapping("/add")
    public String add(){
        double lat = 39.929986;
        double lon = 116.395645;

        List<Person> personList = new ArrayList<Person>(900000);
        for (int i = 100000; i < 1000000; i++) {
            double max = 0.00001;
            double min = 0.000001;
            Random random = new Random();
            double s = random.nextDouble() % (max - min + 1) + max;
            DecimalFormat df = new DecimalFormat("######0.000000");
            // System.out.println(s);
            String lons = df.format(s + lon);
            String lats = df.format(s + lat);
            Double dlon = Double.valueOf(lons);
            Double dlat = Double.valueOf(lats);
            Person person = new Person();
            person.setId(i);
            person.setName("name" + i);
            person.setPhone("phone" + i);
            person.setAddress(dlat + "," + dlon);

            personList.add(person);
        }
        personService.bulkIndex(personList);

//        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.queryStringQuery("spring boot OR 书籍")).build();
//        List<Article> articles = elas、ticsearchTemplate.queryForList(se、archQuery, Article.class);
//        for (Article article : articles) {
//            System.out.println(article.toString());
//        }
        return "add data success";
    }

    /**
     * 添加一亿条记录
     * @return
     */
    @GetMapping("/add2")
    public String add2(){
        double lat = 39.929986;
        double lon = 116.395645;

        List<Person> personList = new ArrayList<Person>(100000);
        for (int i = 1100000; i < 50000000; i++) {
            double max = 0.00001;
            double min = 0.000001;
            Random random = new Random();
            double s = random.nextDouble() % (max - min + 1) + max;
            DecimalFormat df = new DecimalFormat("######0.000000");
            // System.out.println(s);
            String lons = df.format(s + lon);
            String lats = df.format(s + lat);
            Double dlon = Double.valueOf(lons);
            Double dlat = Double.valueOf(lats);
            Person person = new Person();
            person.setId(i);
            person.setName("name" + i);
            person.setPhone("phone" + i);
            person.setAddress(dlat + "," + dlon);

            personList.add(person);
        }
        personService.bulkIndex(personList);

//        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.queryStringQuery("spring boot OR 书籍")).build();
//        List<Article> articles = elas、ticsearchTemplate.queryForList(se、archQuery, Article.class);
//        for (Article article : articles) {
//            System.out.println(article.toString());
//        }
        return "add data success";
    }
    
    /**
     * Created By wuxh on 16:35 2019/06/29.
     * 查询
     */
    @GetMapping("/query")
    public List<Person> query(){
        double lat = 39.929986;
        double lon = 116.395645;
        Long startTime = System.currentTimeMillis();
        List<Person> personList = personService.query(lat,lon);
        System.out.println("query1耗时：" + (System.currentTimeMillis() - startTime)+",return records:"+personList.size());
        return personList;
    }

    /**
     * Created By wuxh on 16:35 2019/06/29.
     * 查询
     */
    @GetMapping("/query2")
    public List<Person> query2(){
        double lat = 39.927986;
        double lon = 116.395645;
        Long startTime = System.currentTimeMillis();
        List<Person> personList = personService.query(lat,lon);
        System.out.println("query2耗时：" + (System.currentTimeMillis() - startTime)+", return records:"+personList.size());
        return personList;
    }
    
    /**
     * Created By wuxh on 16:35 2019/06/29.
     * 查询
     */
    @GetMapping("/delete")
    public void delete(@RequestParam(value="id") String id){
    	System.out.println("to delete"+id);
    	personService.delete(id);
    }
}
