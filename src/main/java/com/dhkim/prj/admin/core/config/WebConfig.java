package com.dhkim.prj.admin.core.config;

import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.dhkim.prj.admin.core.support.aspect.RequestMappingMethodParameterLoggingAspect;
import com.dhkim.prj.admin.core.support.filter.CustomLoggingFilter;
import com.dhkim.prj.admin.core.support.interceptor.PasswordInterceptor;
import com.dhkim.prj.admin.core.support.resolver.PagingResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Bean
	public UrlBasedViewResolver jspViewResolver() {
		UrlBasedViewResolver resolver = new UrlBasedViewResolver();
		resolver.setPrefix("/WEB-INF/jsp/");
		resolver.setSuffix(".jsp");
		resolver.setOrder(1);
		resolver.setRequestContextAttribute("rc");
		resolver.setViewClass(JstlView.class);
		return resolver;
	}
	
//	@Bean
//	public Jxls2View jxls2View() {
//		return new Jxls2View();
//	}
//	
//	@Bean
//	public Jxls2Function jxls2Function() {
//		return new Jxls2Function();
//	}
	
	@Bean
	public FilterRegistrationBean<CustomLoggingFilter> loggingFilter() {
		CustomLoggingFilter loggingFilter = new CustomLoggingFilter();
		
		loggingFilter.setResponseContentTypePrefixesExcludeLogging("text/html", "text/css", "application/javascript", "application/octet-stream");
			
		FilterRegistrationBean<CustomLoggingFilter> registration = new FilterRegistrationBean<>(loggingFilter);
		registration.setOrder(1);//spring.sleuth.web.filter-order가 0이므로 1로 세팅
		
		return registration;
	}
	
	/**
	 * 메시지 프로퍼티 설정
	 * @return
	 */
	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
		source.setBasename("classpath:/messages/messages");
		source.setDefaultEncoding("UTF-8");
		source.setCacheSeconds(60);
		source.setUseCodeAsDefaultMessage(true);
		return source;
	}
	
	@Bean
	public RequestMappingMethodParameterLoggingAspect loggingAspect() {
		return new RequestMappingMethodParameterLoggingAspect();
	}
	
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public PasswordInterceptor passwordInterceptor() {
		return new PasswordInterceptor();
	}
	
	/**
	 * Locale 설정
	 * @return
	 */
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver resolver = new SessionLocaleResolver();
		return resolver;
	}
	
	@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PagingResolver());
    }
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LocaleChangeInterceptor());
		registry.addInterceptor(passwordInterceptor());
	}
	
}
