package com.datadigger.spider.phantomjs.downloader;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
//import org.openqa.selenium.Proxy;
//import org.openqa.selenium.Proxy.ProxyType;
//import org.openqa.selenium.phantomjs.PhantomJSDriver;
//import org.openqa.selenium.phantomjs.PhantomJSDriverService;
//import org.openqa.selenium.remote.CapabilityType;
//import org.openqa.selenium.remote.DesiredCapabilities;

import com.geccocrawler.gecco.downloader.DownloadException;
import com.geccocrawler.gecco.downloader.Downloader;
import com.geccocrawler.gecco.downloader.UserAgent;
import com.geccocrawler.gecco.downloader.proxy.Proxys;
import com.geccocrawler.gecco.downloader.proxy.ProxysContext;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.response.HttpResponse;
import com.geccocrawler.gecco.spider.SpiderThreadLocal;

/**
 * 使用phantomjs下载页面，基于selenium库调用
 *
 * @author xtuhcy
 */
@com.geccocrawler.gecco.annotation.Downloader("phantomJSDownloader")
public class PhantomJSDownloader implements Downloader {
	
	private static final String phantomjsDriverPath = "/phantomjs-2.1.1-windows/bin/phantomjs.exe";

	@Override
	public HttpResponse download(HttpRequest request, int timeout) throws DownloadException {
		return null;
//		DesiredCapabilities dcaps = DesiredCapabilities.phantomjs();
//		String absoluteDriverPath = getClass().getResource(phantomjsDriverPath).getFile();
//        dcaps.setCapability("acceptSslCerts", true);
//        dcaps.setJavascriptEnabled(true);
//        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, absoluteDriverPath);
//        //header
//  		boolean isMobile = SpiderThreadLocal.get().getEngine().isMobile();
//  		dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX+"userAgent", UserAgent.getUserAgent(isMobile));
//  		for(Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
//  			dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX+entry.getKey(), entry.getValue());
//  		}
//        
//        //proxy
//  		HttpHost proxy = null;
//  		Proxys proxys = ProxysContext.get();
//  		boolean isProxy = ProxysContext.isEnableProxy();
//  		if(proxys != null && isProxy) {
//  			proxy = proxys.getProxy();
//  			if(proxy != null) {
//  				Proxy proxyWebDriver = new Proxy();
//  				proxyWebDriver.setProxyType(ProxyType.MANUAL);
//  				proxyWebDriver.setAutodetect(false);
//  		        String proxyStr = proxy.getHostName()+":"+proxy.getPort();
//  		        proxyWebDriver.setHttpProxy(proxyStr);
//  		        dcaps.setCapability(CapabilityType.PROXY, proxyWebDriver);
//  			}
//  		}
//        
//        PhantomJSDriver webDriver = new PhantomJSDriver(dcaps);
//        webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
//        webDriver.manage().timeouts().pageLoadTimeout(timeout/1000, TimeUnit.SECONDS);
//        try {
//        	webDriver.get(request.getUrl());
//        	webDriver.executeScript("return document.documentElement.outerHTML");
//        	webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
//        	//System.out.println(webDriver.getErrorHandler().isIncludeServerErrors());
//			String content = webDriver.getPageSource();
//			HttpResponse resp = new HttpResponse();
//			resp.setStatus(200);
//			resp.setContent(content);
//			//resp.setContentType(contentType);
//			//resp.setCharset(charset);
//			return resp;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		} finally {
//			webDriver.quit();
//		}
	}

	@Override
	public void shutdown() {
		
	}

}
