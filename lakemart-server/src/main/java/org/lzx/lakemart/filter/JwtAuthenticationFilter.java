package org.lzx.lakemart.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        // 放行登录和注册接口
        if (requestURI.equals("/api/user/login") || requestURI.equals("/api/user/register")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                Claims claims = jwtUtil.parseToken(token);
                String role = claims.get("role", String.class);
                Long userId = claims.get("userId", Long.class);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null,
                                Collections.singletonList(new SimpleGrantedAuthority(role)));
                authentication.setDetails(userId);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new ObjectMapper().writeValueAsString(
                        Result.error("无效或过期的令牌")
                ));
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
// package org.lzx.lakemart.filter;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import io.jsonwebtoken.Claims;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import org.lzx.lakemart.result.Result;
// import org.lzx.lakemart.util.JwtUtil;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;
// import java.io.IOException;
// import java.util.Collections;
// import java.util.List;
//
// @Component
// public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//     @Autowired
//     private JwtUtil jwtUtil;
//
//     // 定义公开路径，不进行 token 验证
//     private static final List<String> PUBLIC_PATHS = List.of(
//             "/api/user/login",
//             "/api/user/register",
//             "/api/product/list",
//             "/api/product/detail/"
//     );
//
//     @Override
//     protected void doFilterInternal(HttpServletRequest request,
//                                     HttpServletResponse response,
//                                     FilterChain chain) throws ServletException, IOException {
//         String requestURI = request.getRequestURI();
//
//         if (requestURI.equals("/api/user/login") || requestURI.equals("/api/user/register")) {
//             chain.doFilter(request, response);
//             return;
//         }
//         // 如果是公开路径，直接放行
//         for (String path : PUBLIC_PATHS) {
//             if (requestURI.startsWith(path)) {
//                 chain.doFilter(request, response);
//                 return;
//             }
//         }
//
//         String authHeader = request.getHeader("Authorization");
//         if (authHeader != null && authHeader.startsWith("Bearer ")) {
//             String token = authHeader.substring(7);
//             if (jwtUtil.validateToken(token)) {
//                 Claims claims = jwtUtil.parseToken(token);
//                 String role = claims.get("role", String.class);
//                 Long userId = claims.get("userId", Long.class);
//                 UsernamePasswordAuthenticationToken authentication =
//                         new UsernamePasswordAuthenticationToken(userId, null,
//                                 Collections.singletonList(new SimpleGrantedAuthority(role)));
//                 authentication.setDetails(userId);
//                 SecurityContextHolder.getContext().setAuthentication(authentication);
//             } else {
//                 // token 无效，返回 401
//                 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                 response.setContentType("application/json;charset=UTF-8");
//                 response.getWriter().write(new ObjectMapper().writeValueAsString(
//                         Result.error("无效或过期的令牌")
//                 ));
//                 return;
//             }
//         }
//         chain.doFilter(request, response);
//     }
// }