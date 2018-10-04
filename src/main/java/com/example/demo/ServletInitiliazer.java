package com.example.demo;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

/**
 * Видел в интернете пример, что с помошью такой конфигурации можно
 *  добавлять значения context-param не в web.xml, а в данном бине 
 * 
 * @author legioner
 *
 */
@Configuration
public class ServletInitiliazer implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
//        servletContext.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", "true");
    }

}
