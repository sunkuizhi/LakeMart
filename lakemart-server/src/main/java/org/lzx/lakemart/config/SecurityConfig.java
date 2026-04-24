package org.lzx.lakemart.config;

import org.lzx.lakemart.filter.JwtAuthenticationFilter;
import org.lzx.lakemart.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // // 公开接口（无需登录）
                        // .requestMatchers("/api/user/login", "/api/user/register").permitAll()
                        // .requestMatchers("/api/product/list", "/api/product/detail/**").permitAll()
                        // // 其他所有请求需要认证
                        // .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
// package org.lzx.lakemart.config;
//
// import org.lzx.lakemart.filter.JwtAuthenticationFilter;
// import org.lzx.lakemart.security.UserDetailsServiceImpl;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
// @Configuration
// @EnableMethodSecurity(prePostEnabled = true)
// public class SecurityConfig {
//
//     @Autowired
//     private UserDetailsServiceImpl userDetailsService;
//
//     @Autowired
//     private JwtAuthenticationFilter jwtAuthenticationFilter;
//
//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }
//
//     @Bean
//     public DaoAuthenticationProvider authenticationProvider() {
//         DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//         provider.setUserDetailsService(userDetailsService);
//         provider.setPasswordEncoder(passwordEncoder());
//         return provider;
//     }
//
//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//         return config.getAuthenticationManager();
//     }
//
//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http
//                 .csrf(AbstractHttpConfigurer::disable)
//                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                 .authorizeHttpRequests(auth -> auth
//                         .anyRequest().permitAll()   // 完全放行所有请求
//                         // 公开接口（无需登录）
//                         .requestMatchers(
//                                 "/api/user/login",
//                                 "/api/user/register",
//                                 "/api/product/list",
//                                 "/api/product/detail/**"
//                         ).permitAll()
//                         // 普通用户和管理员都可访问
//                         .requestMatchers("/api/user/**", "/api/cart/**", "/api/address/**", "/api/order/**").hasAnyRole("USER", "ADMIN")
//                         // 管理员专用
//                         .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                         // 其余所有请求需要认证
//                         .anyRequest().authenticated()
//                 )
//                 .authenticationProvider(authenticationProvider())
//                 .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//         return http.build();
//     }
// }
// // package org.lzx.lakemart.config;
// //
// // import org.lzx.lakemart.filter.JwtAuthenticationFilter;
// // import org.lzx.lakemart.security.UserDetailsServiceImpl;
// // import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.context.annotation.Bean;
// // import org.springframework.context.annotation.Configuration;
// // import org.springframework.security.authentication.AuthenticationManager;
// // import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// // import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// // import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// // import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// // import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// // import org.springframework.security.config.http.SessionCreationPolicy;
// // import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// // import org.springframework.security.crypto.password.PasswordEncoder;
// // import org.springframework.security.web.SecurityFilterChain;
// // import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// //
// // @Configuration
// // @EnableMethodSecurity(prePostEnabled = true)
// // public class SecurityConfig {
// //
// //     @Autowired
// //     private UserDetailsServiceImpl userDetailsService;
// //
// //     @Autowired
// //     private JwtAuthenticationFilter jwtAuthenticationFilter;
// //
// //     @Bean
// //     public PasswordEncoder passwordEncoder() {
// //         return new BCryptPasswordEncoder();
// //     }
// //
// //     @Bean
// //     public DaoAuthenticationProvider authenticationProvider() {
// //         DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
// //         provider.setUserDetailsService(userDetailsService);
// //         provider.setPasswordEncoder(passwordEncoder());
// //         return provider;
// //     }
// //
// //     @Bean
// //     public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
// //         return config.getAuthenticationManager();
// //     }
// //
// //     @Bean
// //     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
// //         http
// //                 .csrf(AbstractHttpConfigurer::disable)
// //                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
// //                 .authorizeHttpRequests(auth -> auth
// //                         // 公开接口（无需登录）
// //                         .requestMatchers("/api/user/login", "/api/user/register").permitAll()
// //                         .requestMatchers("/api/product/list", "/api/product/detail/**").permitAll()
// //                         // 其他所有请求需要认证
// //                         .anyRequest().authenticated()
// //                 )
// //                 .authenticationProvider(authenticationProvider())
// //                 .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
// //         return http.build();
// //     }
// // }