package com.lwzw.cloud.config;

import com.lwzw.cloud.component.LoginHandlerIntercepter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Bean
    public WebMvcConfigurerAdapter adapter(){
        WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter(){

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("/index");
                registry.addViewController("/index.html").setViewName("/index");
                registry.addViewController("/main").setViewName("/pages/main");
            }

            //注册拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new LoginHandlerIntercepter()).addPathPatterns("/**")
                        .excludePathPatterns("/user/**","/index.html","/","/js/**","/css/**","/img/**","/static/**");
            }

            /**
             * 添加静态资源文件，外部可以直接访问地址
             * @param registry
             */
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                //第一个方法设置访问路径前缀，第二个方法设置资源路径
                registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
            }
        };
        return adapter;
    }
}
