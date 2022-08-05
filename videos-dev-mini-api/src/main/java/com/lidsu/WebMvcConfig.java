package com.lidsu;


import com.lidsu.controller.interceptor.MiniInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("file:E:/github/wxchatapp/videos_dev/");
    }
    @Bean
    public MiniInterceptor miniInterceptor(){
        return new MiniInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(miniInterceptor()).addPathPatterns("/user/**")
                                                  .addPathPatterns("/bgm/**")
                                                  .addPathPatterns("/video/upload","/video/uploadCover")
                .excludePathPatterns("/user/queryPublisher")
                .excludePathPatterns("/user/beyourfans")
                .excludePathPatterns("/user/dontbeyourfans");

        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
