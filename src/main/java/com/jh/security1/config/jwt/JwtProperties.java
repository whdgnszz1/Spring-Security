package com.jh.security1.config.jwt;

public class JwtProperties {
    public static final String SECRET = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd5e37eb712d013b4445e884898da28047151d0e56f8dc6292773603d0d6aabbdd5e37eb712d013b444"; // JWT 서명에 사용할 비밀 키
    public static final int EXPIRATION_TIME = 864000000; // 10일 (1/1000초 단위)
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
