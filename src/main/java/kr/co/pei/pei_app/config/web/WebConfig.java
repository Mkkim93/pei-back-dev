package kr.co.pei.pei_app.config.web;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import kr.co.pei.pei_app.config.exception.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(new LogFilter());
        filterRegBean.setOrder(1);
        filterRegBean.addUrlPatterns("/*");
        filterRegBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);
        return filterRegBean;
    }
}
