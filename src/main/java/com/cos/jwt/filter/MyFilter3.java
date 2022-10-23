package com.cos.jwt.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class MyFilter3 implements Filter {
    //
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //
        log.info("------------------------------> 필터3");
        HttpServletRequest req =  (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        log.info("------------------------------> req.getMethod : " + req.getMethod());
        // 토큰 : cos 을 만들어야 함, id/pw 로 정상적으로 들어와서 로그린이 정상적으로 되면 토큰을 만들어주고 그 것을 응답해준다
        // 요청 할때 마다 header에 Authorization에 value 값으로 토큰을 넣는다
        // 해당 토큰을 끄집어 내어 리 토킁이 내가 만든 토큰이 맞는지 검증하면 됨 (RSA, HS256)
        if(req.getMethod().equals("POST")) {
            log.info("------------------------------> POST 요청 됨 !!!!!");
            String headerAuth = req.getHeader("Authorization");
            log.info("------------------------------> headerAuth : " + headerAuth);

            // Authorization 이 "cod" 인 경우만 chain을 타게 할 것이고
            if(headerAuth.equals("cos")) {
                log.info("------------------------------> 인증 됨!!!! ");
                chain.doFilter(req, res);
            } else { // Authorization 이 "cod"가 아닌 경우 브라우저에 "인증안됨!!!!!" 출력
                PrintWriter out = res.getWriter();
                out.println("인증안됨!!!!!");
                return;
            }
        }
        chain.doFilter(request, response); // 계속 Process를 진향해라
    }
}
