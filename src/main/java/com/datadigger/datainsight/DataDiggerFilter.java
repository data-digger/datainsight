package com.datadigger.datainsight;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMethod;

@Order(1)
@WebFilter(filterName = "responseFilter", urlPatterns = "/*")
public class DataDiggerFilter implements Filter {
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
          throws IOException, ServletException {
	  HttpServletResponse response1 = (HttpServletResponse) servletResponse;
      HttpServletRequest request1 = (HttpServletRequest) servletRequest;
      response1.setHeader("Access-Control-Allow-Origin", "*");
      response1.setHeader("Access-Control-Allow-Credentials", "true");
      response1.setHeader("Access-Control-Allow-Methods", "*");
      response1.setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token");
      response1.setHeader("Access-Control-Expose-Headers", "*");

      if (request1.getMethod().equals( RequestMethod.OPTIONS.toString())){
          System.out.println("-----检查------");
          return;
      }
      filterChain.doFilter(servletRequest,servletResponse);
  }

  @Override
  public void destroy(){

  }
}

