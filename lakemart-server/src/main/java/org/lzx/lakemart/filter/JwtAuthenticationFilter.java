package org.lzx.lakemart.filter;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.lzx.lakemart.mapper.UserMapper;
import org.lzx.lakemart.model.entity.User;
import org.lzx.lakemart.result.Result;
import org.lzx.lakemart.security.SecurityUser;
import org.lzx.lakemart.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT 认证过滤器
 * 负责解析请求头中的 JWT token，并设置 SecurityContext 中的认证信息
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;   // 注入 UserMapper，用于根据 userId 查询用户

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        // 放行登录和注册接口（这些接口不需要 token）
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

                // 根据 userId 从数据库查询用户，构建 SecurityUser
                User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, userId));
                if (user == null) {
                    // 用户不存在，返回未授权
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(new ObjectMapper().writeValueAsString(
                            Result.error("用户不存在")
                    ));
                    return;
                }

                // 使用 SecurityUser 作为 principal，而不是直接使用 userId
                SecurityUser securityUser = new SecurityUser(user);

                // 构建 Authentication 对象，principal = SecurityUser，包含权限信息
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(securityUser, null,
                                Collections.singletonList(new SimpleGrantedAuthority(role)));
                authentication.setDetails(securityUser);  // 可选，可存额外信息
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // token 无效或过期
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new ObjectMapper().writeValueAsString(
                        Result.error("无效或过期的令牌")
                ));
                return;
            }
        }
        // 继续执行后续过滤器
        chain.doFilter(request, response);
    }
}