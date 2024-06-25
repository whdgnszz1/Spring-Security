package com.jh.security1.config.jwt;

public class JwtProperties {
    public static final String SECRET = "YourSecretKey"; // JWT 서명에 사용할 비밀 키
    public static final int EXPIRATION_TIME = 864000000; // 10일 (1/1000초 단위)
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
