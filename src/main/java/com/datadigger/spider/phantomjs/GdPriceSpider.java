package com.datadigger.spider.phantomjs;

import java.util.List;

import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.annotation.Gecco;
import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.annotation.PipelineName;
import com.geccocrawler.gecco.pipeline.Pipeline;
import com.geccocrawler.gecco.spider.HtmlBean;

@PipelineName("gdPriceSpider")
@Gecco(matchUrl="http://www.gdprice.org", downloader="phantomJSDownloader", timeout=60000, pipelines="gdPriceSpider")
public class GdPriceSpider implements HtmlBean, Pipeline<GdPriceSpider>  {

	private static final long serialVersionUID = -377053120283382723L;
	
	@HtmlField(cssPath="#menu")
	private String menu;
	
	@HtmlField(cssPath="#dbphq1 tbody")
	private String table;
	
	@HtmlField(cssPath="#dbphq1 tbody tr")
	private List<Item> items;
	
	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}
	
	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
	
	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("-----------------------------------------------------------------------------\n")
			  .append("GdPriceSpider(size=")
			  .append(items.size())
			  .append("):\n");
		for(Item item: items) {
			result.append(item).append("\n");
		}
		result.append("-----------------------------------------------------------------------------");
		return result.toString();
	}


	public static class Item implements HtmlBean {
		
		private static final long serialVersionUID = 5243013123370386328L;
		
		@HtmlField(cssPath="td:eq(0)")
		private String name;
		
		@HtmlField(cssPath="td:eq(1)")
		private String category;
		
		@HtmlField(cssPath="td:eq(2)")
		private String avgPrice;
		
		@HtmlField(cssPath="td:eq(3)")
		private String unit;
		
		@HtmlField(cssPath="td:eq(4)")
		private String date;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getAvgPrice() {
			return avgPrice;
		}

		public void setAvgPrice(String avgPrice) {
			this.avgPrice = avgPrice;
		}

		public String getUnit() {
			return unit;
		}

		public void setUnit(String unit) {
			this.unit = unit;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		@Override
		public String toString() {
			return "Item [name=" + name + ", category=" + category
					+ ", avgPrice=" + avgPrice + ", unit=" + unit + ", date="
					+ date + "]";
		}
		
	}

	public static void main(String[] args) throws Exception {
		GeccoEngine.create()
		.classpath("com.datadigger.spider.phantomjs")
		//开始抓取的页面地址
		.start("http://www.gdprice.org")
		//开启几个爬虫线程
		.thread(1)
		.run();
	}

	@Override
	public void process(GdPriceSpider bean) {
		System.out.println(bean);
	}
}