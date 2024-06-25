package com.jh.security1.config;

import com.jh.security1.config.jwt.JwtAuthenticationFilter;
import com.jh.security1.config.jwt.JwtAuthorizationFilter;
import com.jh.security1.config.oauth.PrincipalOauth2UserService;
import com.jh.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration // IoC 빈(bean)을 등록
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize 어노테이션 활성화
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CorsConfig corsConfig;

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/user/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                        .requestMatchers("/api/v1/manager/**").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll());

        http.addFilterBefore(corsConfig.corsFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtAuthorizationFilter(authenticationManager, userRepository), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
