package com.cos.jwt.config;

import com.cos.jwt.config.jwt.JwtAuthenticationFilter;
import com.cos.jwt.filter.MyFilter1;
import com.cos.jwt.filter.MyFilter3;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //
    private final CorsFilter corsFilter;

    //@Bean 부여하면 해당 메소드의 리터되는 오브젝트를 Ioc로 등록
    @Bean
    public BCryptPasswordEncoder encoderPwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 시큐리티 필터에서 제일먼저 내가 만든 필토를 제일먼저 실행시키고자 할때 라애 처럼 쓰면 됨
        // http.addFilterBefore(new MyFilter1(), BasicAuthenticationFilter.class);
        // 시큐리티 필터체인에 등록한 사용자 필터가 가장 최상위 'SecurityContextHolderFilter' 보다 먼저 실행
        //http.addFilterBefore(new MyFilter3(), SecurityContextHolderFilter.class);
        // csrf 사용하지 않겠다
        http.csrf().disable();
        // Session을 사용하지 않겠다는 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter) // 무조건 인증이 필요한 경우는 Filter에 등록 해야 함, Controller에 @CrossOrigin 붙이는 것은 인증이 필료없는 Controller,
                .formLogin().disable() // form tag 로그인은 않쓴다
                // http Header의 Authorization에 ID,PW 를 담아 요청 하는 방식을 않쓴다 는 설정, https 는 ID, PW 는 암호화 됨
                .httpBasic().disable() // 기본적인 http 요청 방식은 아예 안쓴다 ( http Header의 Authorization에 ID,PW 를 담는 Basic 방식은 않쓴다 )
                .addFilter(new JwtAuthenticationFilter(authenticationManager())) // AuthenticationManager Parameter 반드시 넘겨주어야 한다.
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                // ROLE_USER, ROLE_MANAGER, ROLE_ADMIN 권한을 가진 사용자는 접근가능
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                // ROLE_MANAGER, ROLE_ADMIN 권한을 가진 사용자는 접근가능
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                // ROLE_ADMIN 권한을 가진 사용자는 접근가능
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll() // 그외 Request는 모두 접근 가능
                ;

    }
}
