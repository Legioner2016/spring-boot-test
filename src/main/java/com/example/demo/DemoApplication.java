package com.example.demo;


import java.util.EnumSet;

import javax.faces.webapp.FacesServlet;
import javax.servlet.DispatcherType;

import org.ocpsoft.rewrite.servlet.RewriteFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.example.dao.EntityDao;

/**
 * Стартовый класс spring-boot приложения
 * 
 * @author legioner
 *
 */
@SpringBootApplication(scanBasePackages = {"com.example.database", "com.example.dao", "com.example.controller", "com.example.demo"},
					exclude = {SecurityAutoConfiguration.class})
@EntityScan( basePackages = {"com.example.database"}) //Без этого EntityManager не видит сущностей
public class DemoApplication  extends SpringBootServletInitializer {

	//В случае дополнительных настроек иметь из бина конфигурации доступ к application.properties
	@Autowired
	private Environment environment;
	
	//Основной репозиторий. С целью тестирования здесь был добавлен 
	@Autowired
	private EntityDao dao;

	//Запуск
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	
	/*
	 * Без двух следующих бинов jsf - ы не хотели работать 
	 */
	@Bean
    public ServletRegistrationBean servletRegistrationBean() {
        FacesServlet servlet = new FacesServlet();
        return new ServletRegistrationBean(servlet, "*.jsf");
    }
	
	@Bean
    public FilterRegistrationBean rewriteFilter() {
        FilterRegistrationBean rwFilter = new FilterRegistrationBean(new RewriteFilter());
        rwFilter.setDispatcherTypes(EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST,
                DispatcherType.ASYNC, DispatcherType.ERROR));
        rwFilter.addUrlPatterns("/*");
        return rwFilter;
    }
	
	

}
