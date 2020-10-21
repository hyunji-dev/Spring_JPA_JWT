package com.cos.jwt.config.filter;
/*
 * 2020.10.21-5
 * 선행: FilterConfig.java
 * 후행: JwtAuthenticationFilter.java
 */

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;

public class CorsFilter implements Filter {
	public static final String TAG = "MyFilter1 : ";
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("CORS 필터 작동");
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setHeader("Access-Control-Allow-Origin", "*");
		chain.doFilter(request, response);
	}
}
