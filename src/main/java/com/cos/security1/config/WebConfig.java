package com.cos.security1.config;

import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    //패스워드 암호화 빈으로 등록
    @Bean   //->해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다. -> 어디서든 쓸 수 있다 (Autowired 해서)
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        //머스테치 재설정
        MustacheViewResolver resolver = new MustacheViewResolver();
        resolver.setCharset("UTF-8");
        resolver.setContentType("text/html;charset=UTF-8");
        //classpath 앞까지 프로젝트
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        registry.viewResolver(resolver);
    }
}
