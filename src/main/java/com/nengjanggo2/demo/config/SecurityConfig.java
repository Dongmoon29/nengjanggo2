package com.nengjanggo2.demo.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//Spring Security 설정
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //인증과정 없이 인가될 수 있는 루트
        http
                .authorizeRequests()
                .mvcMatchers( "/", "/login", "/registration", "/check-email-token").permitAll()
                .anyRequest().authenticated();
        //로그인 페이지 설정
        http
                .formLogin()
                .loginPage("/login").permitAll();

        //로그아웃 설정
        http
                .logout()
                .logoutSuccessUrl("/");

    }
    //css, js 파일 접근 권한 설정
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());

    }
}

