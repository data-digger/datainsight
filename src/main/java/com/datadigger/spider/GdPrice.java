package com.datadigger.spider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.datadigger.datainsight.bean.GridData;
import com.datadigger.datainsight.domain.DataSource;
import com.datadigger.datainsight.util.ConnectionPool;
import com.datadigger.datainsight.util.JdbcUtil;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator;
import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.annotation.Ajax;
import com.geccocrawler.gecco.annotation.Gecco;
import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.annotation.JSONPath;
import com.geccocrawler.gecco.annotation.PipelineName;
import com.geccocrawler.gecco.annotation.Text;
import com.geccocrawler.gecco.pipeline.Pipeline;
import com.geccocrawler.gecco.request.HttpGetRequest;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;


@PipelineName("GdPrice")
@Gecco(matchUrl="http://www.gdprice.org", pipelines="GdPrice")
public class GdPrice implements HtmlBean, Pipeline<GdPrice> {
	
	private static final long serialVersionUID = 6683895914723213684L;
	
	//@Ajax(url="http://www.gdprice.org/ProductPriceFrontAct/getIndexProductPriceData.jspx")
	@Ajax(url="http://www.gdprice.org/clz/list.jspx?typeid=40000000&_=1521617794207")
	//@HtmlField(cssPath="body > div.centbox > div:nth-child(5) > div:nth-child(2) > div.biglist > ul > li:nth-child(1)")
	private Product product;
	
//	private List<Item> items;
//	
//	public List<Item> getItems() {
//		return items;
//	}
//
//	public void setItems(List<Item> items) {
//		this.items = items;
//	}
//
//	public static class Item implements HtmlBean {
//		
//		private static final long serialVersionUID = 5243013123370386328L;
//
//		@JSONPath("$.pageNo")
//		private String name;
//		
//		
//
//		public String getName() {
//			return name;
//		}
//
//		public void setName(String name) {
//			this.name = name;
//		}
//
//	}

	@Override
	public void process(GdPrice bean) {
		System.out.println("Start process crawler data....");
		Product product = bean.getProduct();
		JSONArray arr = product.getArr();
		
		DataSource ds = new DataSource();
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
		
		for(int i =0; i < arr.size(); i++){
			
			JSONObject obj = (JSONObject)arr.get(i);
			String sql = "insert into t_price(cname,price,update_time)values('"+obj.getString("cname") +"'," + obj.getBigDecimal("price") + ",'" + obj.getString("priceTime") + "')";
			JdbcUtil.executeInsert(ds, sql);
		    System.out.println(sql);
		}
	}

	public static void main(String[] args) {
//		GeccoEngine.create()
//		.classpath("com.datadigger.spider")
//		.start("http://www.gdprice.org/")
//		.run();
		HttpRequest request = new HttpGetRequest("http://www.gdprice.org");
		request.setCharset("GBK");
		GeccoEngine.create()
		.classpath("com.datadigger.spider")
		//开始抓取的页面地址
		.start(request)
		//开启几个爬虫线程
		.thread(1)
		.run();
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
