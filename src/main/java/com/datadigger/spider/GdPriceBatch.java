package com.datadigger.spider;

import java.util.Calendar;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.datadigger.datainsight.domain.DataSource;
import com.datadigger.datainsight.util.JdbcUtil;
import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.annotation.Ajax;
import com.geccocrawler.gecco.annotation.Gecco;
import com.geccocrawler.gecco.annotation.JSONPath;
import com.geccocrawler.gecco.annotation.PipelineName;
import com.geccocrawler.gecco.annotation.Request;
import com.geccocrawler.gecco.annotation.RequestParameter;
import com.geccocrawler.gecco.pipeline.Pipeline;
import com.geccocrawler.gecco.request.HttpGetRequest;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;
import com.geccocrawler.gecco.spider.JsonBean;


@PipelineName("GdPriceBatch")
@Gecco(matchUrl="http://www.gdprice.org/?{pageNo}?{startDate}?{endDate}", pipelines="GdPriceBatch")
public class GdPriceBatch implements HtmlBean, Pipeline<GdPriceBatch> {
	
	private static final DataSource ds  = new DataSource();
	
	static {
		ds.setAlias("demo_db");
		ds.setDbCharset("UTF-8");
		ds.setDesc("a database for demo datainsight");
		ds.setDriver("com.mysql.jdbc.Driver");
		ds.setDriverType("MySQL");
		ds.setId("DS.demo_db");
		ds.setMaxConnection(100);
		ds.setName("demo_db");
		ds.setUser("root");
		ds.setPassword("admin");
		ds.setTransactionIsolation(-1);
		ds.setUrl("jdbc:mysql://localhost:3306/demo_db?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
		ds.setValidationQuery("SELECT 1 FROM DUAL");
	}
	public GdPriceBatch() {
		super();
		
		
	}

	private static final long serialVersionUID = 6683895914723213684L;
	
	/**
	 * 日期
	 */
	@RequestParameter("time")
	private String time;
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Ajax(url="http://www.gdprice.org/clz/pageList.jspx?pageNo={pageNo}&queryName=&startTime=&endTime=&isLike=true")
	private ProductList productList;
	
	

	@Override
	public void process(GdPriceBatch bean) {
		System.out.println("Start process crawler data....");
		ProductList pList = bean.getProductList();
		JSONArray arr = pList.getArr();
		
		for(int i =0; i < arr.size(); i++){
			
			JSONObject obj = (JSONObject)arr.get(i);
			String sql = "insert into t_price(cname,price,update_time)values('"+obj.getString("name") +"'," + obj.getBigDecimal("price") + ",'" + obj.getString("time") + "')";
			JdbcUtil.executeInsert(ds, sql);
		    System.out.println(sql);
		}
	}

	public ProductList getProductList() {
		return productList;
	}

	public void setProductList(ProductList productList) {
		this.productList = productList;
	}

	public static void main(String[] args) {
//		GeccoEngine.create()
//		.classpath("com.datadigger.spider")
//		.start("http://www.gdprice.org/")
//		.run();
//		String[] dateArray= {"2018-03-21",
//		                     "2018-03-20",
//		                     "2018-03-19",
//		                     "2018-03-18",
//		                     "2018-03-17",
//		                     "2018-03-16",
//		                     "2018-03-15",
//		                     "2018-03-14",
//		                     "2018-03-13",
//		                     "2018-03-12",
//		                     "2018-03-11",
//		                     "2018-03-10",
//		                     "2018-03-09"};
		String startDate = "2018-02-01";
		String endDate = "2018-02-28";
		int page = 2500;
		for(int i = 621; i < page; i++){
		       
		       System.out.println("pageNo......................................" + i);
		       System.out.println("pageNo......................................" + i);
		       System.out.println("pageNo......................................" + i);
		       System.out.println("pageNo......................................" + i);
		       System.out.println("pageNo......................................" + i);
		       
		HttpRequest request = new HttpGetRequest("http://www.gdprice.org/?"+ i +"?" + startDate +"?" + endDate);
		request.setCharset("GBK");
		GeccoEngine.create()
		.classpath("com.datadigger.spider")
		//开始抓取的页面地址
		.start(request)
		//开启几个爬虫线程
		.thread(1)
		.run();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}

}
