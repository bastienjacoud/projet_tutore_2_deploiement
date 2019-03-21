package com.projet2.api;

import com.projet2.api.Filters.ParametresChoixFilter;
import com.projet2.api.Filters.ParametresPlanningFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${management.endpoints.web.cors.allowed-origins}")
    String origins;


    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/api/**")
                .allowedOrigins(origins)
                .allowedMethods("PUT", "DELETE", "POST", "GET", "OPTIONS")
                .allowedHeaders("Content-Type", "identificationToken")
                .allowCredentials(false).maxAge(3600);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/notFound").setViewName("forward:/index.html");
    }


    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
        return container -> container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,
                "/notFound"));
    }

    @Bean
    public FilterRegistrationBean<Filter> choiceFilterRegistration() {
        return getRegistration(choicesFilter(), "choicesFilter", "/api/choices/*", 1);
    }

    @Bean
    public FilterRegistrationBean<Filter> schedulesFilterRegistration() {
        return getRegistration(schedulesFilter(), "schedulesFilter", "/api/schedules/*", 2);
    }

    @Bean(name = "choicesFilter")
    public Filter choicesFilter() {
        return new ParametresChoixFilter();
    }

    @Bean(name = "schedulesFilter")
    public Filter schedulesFilter() {
        return new ParametresPlanningFilter();
    }

    private FilterRegistrationBean<Filter> getRegistration(Filter filter, String name, String url, int order){
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.addUrlPatterns(url);
        registration.setName(name);
        registration.setOrder(order);
        return registration;
    }
}
