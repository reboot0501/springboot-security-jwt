package com.cos.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있는데
// "/login" 이리고 요청해서 username, password를 POST롷 전송하면
// UsernamePasswordAuthenticationFilter 가 동작 함
// 그러나 스프링 시큐리티 SecurityConfig에서 .formLogin().disable() 라고 설정하였기 땨문에 동작하지 않음
// "/login" 요청이 받아지게 하려면 아래의 정의한 Filter를 스프링 시큐리티에 등록하면 된디.
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    //
    private final AuthenticationManager authenticationManager;
    // "/login" 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //
        log.info("----------------------------------> JwtAuthenticationFilter.attemptAuthentication : 로그인 사도 중");
        // 1. username, password를 받아서
        Authentication authentication = null;
        try {
//            x-www-form-encoded 방식에서 header 파싱 방법
//            BufferedReader br = request.getReader();
//            String input = null;
//            while ((input = br.readLine()) != null) {
//                log.info("----------------------------------> input : " + input);
//            }
            // Json 파싱 방법
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            log.info("----------------------------------> user : " + user.toString());

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            // PrincipalDetailsService 의 loadUserByUsername() 함수가 실행됨
            // authentication에는 로그인한 정보가 담겨 있다
            authentication = authenticationManager.authenticate(authenticationToken);

            // authentication 객체가 session 영역에 저장 됨 ==> 로그인이 되었다는 얘기 임
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            // principalDetails.getUsername 장 출력되면 로그인이 잘 되었다는 애기임
            log.info("-----------[로그인 완료됨을 체크]--------------> principalDetails.getUsername : " + principalDetails.getUsername());
            // authentication 객체를 session 영역에 저장하려면 return 해주면 됨
            // return 의 이유는 권한관리를 security가 대신 해주기 때문에 편리하게 하려고 return 함
            // 굳이 JWT 토튼을 사용하면서 세션을 만들이유는 없음, 근데 단지 권한 처리때문에 session에 넣어줌
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 실행
    // successfulAuthentication 에서 JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해주면 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        //
        PrincipalDetails principalDetailis = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject(principalDetailis.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .withClaim("id", principalDetailis.getUser().getId())
                .withClaim("username", principalDetailis.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        // response Header에 Jwt 토큰 셋팅
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
    }
}
