package com.jh.security1.config.jwt;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jh.security1.config.auth.PrincipalDetails;
import com.jh.security1.dto.LoginRequestDto;
import com.jh.security1.model.User;
import com.jh.security1.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        System.out.println("JwtAuthenticationFilter : 진입");

        ObjectMapper om = new ObjectMapper();
        LoginRequestDto loginRequestDto = null;
        try {
            loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("JwtAuthenticationFilter : " + loginRequestDto);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword());

        System.out.println("JwtAuthenticationFilter : 토큰생성완료");

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("Authentication : " + principalDetailis.getUser().getUsername());
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        PrincipalDetails principalDetailis = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = Jwts.builder()
                .setSubject(principalDetailis.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .claim("id", principalDetailis.getUser().getId())
                .claim("username", principalDetailis.getUser().getUsername())
                .signWith(SignatureAlgorithm.HS512, JwtProperties.SECRET)
                .compact();

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
    }
}
