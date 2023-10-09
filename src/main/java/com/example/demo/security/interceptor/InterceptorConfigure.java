package com.example.demo.security.interceptor;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfigure implements WebMvcConfigurer {
    private final DomainUrlInterceptor domainUrlInterceptor;
    private final JwtInterceptor jwtInterceptor;
    private final LogOutInterceptor logOutInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(domainUrlInterceptor)
                .addPathPatterns("/**")
                .order(0);
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login/**","/principal/**")
                .order(1);
        registry.addInterceptor(logOutInterceptor)
                .addPathPatterns("/login/**","/join/**")
                .order(2);

    }
}

