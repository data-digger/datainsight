package com.datadigger.spider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.datadigger.datainsight.bean.GridData;
import com.datadigger.datainsight.domain.DataSource;
import com.datadigger.datainsight.util.ConnectionPool;
import com.datadigger.datainsight.util.JdbcUtil;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator;
import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.annotation.Gecco;
import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.annotation.PipelineName;
import com.geccocrawler.gecco.annotation.Text;
import com.geccocrawler.gecco.pipeline.Pipeline;
import com.geccocrawler.gecco.spider.HtmlBean;


@PipelineName("GdPrice")
@Gecco(matchUrl="http://www.gdprice.org/", pipelines="GdPrice")
public class GdPrice implements HtmlBean, Pipeline<GdPrice> {
	
	private static final long serialVersionUID = 6683895914723213684L;
	
	@HtmlField(cssPath=".biglist ")
	private List<Item> items;
	
	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public static class Item implements HtmlBean {
		
		private static final long serialVersionUID = 5243013123370386328L;

		@HtmlField(cssPath=".biglist a ")
		private String name;
		
		@Text
		@HtmlField(cssPath="a")
		private String tag;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}
		
	}

	@Override
	public void process(GdPrice bean) {
		List<Item> items = bean.getItems();
		for(int i = 0; i < items.size(); i++)
		System.out.println(i + "..." + items.get(i).getName());
//		DataSource ds = new DataSource();
//		ds.setAlias("demo_db");
//		ds.setDbCharset("UTF-8");
//		ds.setDesc("a database for demo datainsight");
//		ds.setDriver("com.mysql.jdbc.Driver");
//		ds.setDriverType("MySQL");
//		ds.setId("DS.demo_db");
//		ds.setMaxConnection(100);
//		ds.setName("demo_db");
//		ds.setUser("root");
//		ds.setPassword("admin");
//		ds.setTransactionIsolation(-1);
//		ds.setUrl("jdbc:mysql://localhost:3306/demo_db?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
//		ds.setValidationQuery("SELECT 1 FROM DUAL");
//		
//		for(int i =0; i < bean.getItems().size(); i++){
//			String uuid = UUID.randomUUID().toString();
//			Item it = bean.getItems().get(i);
//			String sql = "insert into t_sina_news(id,title,ctime)values('"+uuid +"','" + it.getUrl() + "',now())";
//			JdbcUtil.executeInsert(ds, sql);
//		    System.out.println(it.getUrl());
//		}
	}

	public static void main(String[] args) {
		GeccoEngine.create()
		.classpath("com.datadigger.spider")
		.start("http://www.gdprice.org/")
		.run();
	}
}
