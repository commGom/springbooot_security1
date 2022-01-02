package com.cos.security1.config;

import com.cos.security1.auth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//1. code를 받고 (인증)
//2. access 토큰 받음 (권한)
//        →access 토큰을 받은 후 구글 로그인한 사용자에 대한 권한이 생김
//3. 사용자 프로필 정보를 가져오고
//4. 그 정보를 토대로 자동으로 회원가입 시키거나 정보가 부족할 경우 → 추가적인 정보들을 받아 회원가입하기
// 구글로그인이 되면 코드를 받는 것이 아니고 (Access Token 과 사용자 프로필 정보를 한 번에 받는다)
@Configuration
@EnableWebSecurity  //스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //@Secured Annotaion @PreAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() //인증만 되면 들어갈 수 있음.
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/") //login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행해준다 /login 안만들어도됨
                .and()
                .oauth2Login()
                .loginPage("/loginForm")//구글 로그인 완료된 뒤의 후처리가 필요하다.
                .userInfoEndpoint()
                .userService(principalOauth2UserService);

    }
}
