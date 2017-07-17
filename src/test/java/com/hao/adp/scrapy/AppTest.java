package com.hao.adp.scrapy;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RegexpQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hao.adp.scrapy.base.page.PageResult;
import com.hao.adp.scrapy.base.util.RedisCacheUtil;
import com.hao.adp.scrapy.reduce.dao.IMemberDao;
import com.hao.adp.scrapy.reduce.dto.MemberCondition;
import com.hao.adp.scrapy.reduce.entity.Member;
import com.hao.adp.scrapy.reduce.service.IConcernService;
import com.hao.adp.scrapy.reduce.service.IMemberService;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
public class AppTest {

	private static Log logger = LogFactory.getLog(AppTest.class);
	@Resource
	private RedisCacheUtil redisCacheUtil;
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	@Resource
	private IConcernService concernService;
	@Resource
	private IMemberService memberService;
	@Resource
	private IMemberDao memberDao;
	@org.junit.Test
	public void testRedis() {
		String key  = "key";
		System.out.println( redisTemplate.opsForValue().setIfAbsent(key, 1));
		System.out.println(redisTemplate.opsForValue().get(key));
		System.out.println(redisTemplate.opsForValue().setIfAbsent(key, 1));
		System.out.println(redisTemplate.expire(key, 100, TimeUnit.SECONDS));
	}

	@org.junit.Test
	public void testTrascation() {
		concernService.testTranscation();
	}

	public static void main(String[] args) throws InterruptedException {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setMaxPoolSize(8);
		executor.setCorePoolSize(1);
		executor.initialize();

		for (int i = 0; i < 5000; i++) {
			executor.execute(new Runnable() {

				@Override
				public void run() {
					logger.info("test");
				}
			});
		}
		TimeUnit.SECONDS.sleep(2);
	}
	
	private TransportClient client;
	
	
	@SuppressWarnings("resource")
	@Before
	public void beforeOne(){
//		Settings esSettings = Settings.builder()
//				.put("cluster.name", "worke")
////		        .put("client.transport.ignore_cluster_name", false)
////		        .put("node.client", true)
//		        .put("client.transport.sniff", true)
//			    .build();
//		client = new PreBuiltTransportClient(esSettings).addTransportAddresses(new InetSocketTransportAddress(new InetSocketAddress("127.0.0.1", 9300)));
		System.out.println("connection success");
		
	}
	private BulkProcessor bulkProcessor;
	
	
	public BulkProcessor openBulk(){
		bulkProcessor = BulkProcessor.builder(client,
                new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId,
                                           BulkRequest request) {

                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request,  BulkResponse response) {

                    }
                    //设置ConcurrentRequest 为0，Throwable不抛错
                    @Override
                    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                        System.out.println("happen fail = " + failure.getMessage() + " cause = " +failure.getCause());
                    }
                })
                .setBulkActions(100000)
                .setBulkSize(new ByteSizeValue(150, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(60))
                .setConcurrentRequests(5)
                .build();
		return bulkProcessor;
	}
	
	public void closeBulk(){
		bulkProcessor.close();
	}
	
	@Test
	public void doChait() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			mapper.setSerializationInclusion(Include.NON_NULL);
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        openBulk();
	        MemberCondition condition = new MemberCondition();
	        PageResult<Member> pageResult = new PageResult<>();
//	        pageResult
	        int count = memberDao.getMemberByPageCount(condition);
	        pageResult.setRows(count);
	        pageResult.setPageSize(1000);
	        for (int i = 1; i <= pageResult.getAllPages(); i++) {
	        	pageResult.setCurrentPage(i);
				RowBounds rowBounds = new RowBounds(pageResult.getCurrentPageIndex(),pageResult.getPageSize());
				List<Member> list = memberDao.listMemberByPage(rowBounds,condition);
				for (Member item : list) {
					JSONObject jsonObj  =  JSON.parseObject(item.getMemberData());
					jsonObj.put("user_name", item.getUserName());
					jsonObj.put("sex", item.getSex());
					jsonObj.put("concerns",item.getConcerns());
					jsonObj.put("follows",item.getFollows());
					jsonObj.put("introduce", item.getIntroduce());
					jsonObj.put("describes", item.getDescribe());
					jsonObj.put("pic_url", item.getPicUrl());
					bulkProcessor.add(new IndexRequest("member_index", "member", item.getUserId()).source(jsonObj));
				}
				
			}
	        closeBulk();
	        System.out.println("end..............................");
		} catch (Exception e) {
			logger.error("写入es报错" + e.getMessage(), e);
		}
	}
	@Test
	public void bulckUpdate(){
		RegexpQueryBuilder query = QueryBuilders.regexpQuery("locations.name.keyword", ".*市");
		SearchResponse result = client.prepareSearch().setQuery(query).setSize(10000).get();
	    SearchHits hits = result.getHits();
	    long n = hits.getTotalHits();
	    System.out.println(n);
	    for (SearchHit searchHit : hits) {
			Map<String, Object> map = searchHit.getSourceAsMap();
			map.get("locations");
			
			System.out.println(JSON.toJSONString(map));
		}
		
	}
}
