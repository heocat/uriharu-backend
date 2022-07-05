package hh.com.uriharu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

import hh.com.uriharu.security.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http 시큐리티 빌더
        http.cors() // WebMvcConfig 에서 이미 설정 했으므로 기본 cors설정
        .and()
        .csrf().disable() //csrf는 현재 사용하지 않아서 disdable
        .httpBasic().disable() //token을 사용하므로 basic 인증 diable
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //session기반이 아님을 선언
        .and()
        .authorizeRequests() // /와 /auth/** 경로는 인증 안해도 됨
        .antMatchers("/","/auth/**").permitAll()
        .anyRequest().authenticated(); // /와 /auth/** 이외의 모든 경로는 인증해야함

        http.addFilterAfter( //filter 등록
        //매 요청마다 CorsFilter실행 한 뒤 jwtAuthenticationFilter 실행한다.
            jwtAuthenticationFilter
            , CorsFilter.class); 

    }

    
}
