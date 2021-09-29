package com.example.reactor_bug;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

/**
* Config
*/
@Configuration
public class Config {

	@Bean
	public FilterRegistrationBean<ShallowEtagHeaderFilter> filterRegistrationBean() {
		final FilterRegistrationBean<ShallowEtagHeaderFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new ShallowEtagHeaderFilter());
		bean.setUrlPatterns(Arrays.asList("*"));
		return bean;
	}

	
}
