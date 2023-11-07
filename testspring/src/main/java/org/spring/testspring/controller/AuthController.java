package org.spring.testspring.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.testspring.requset.Login;
import org.spring.testspring.response.SessionResponse;
import org.spring.testspring.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;


@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    private final String key = "9lWyi+432AUQSVBCmmXMrYWIM7j88f5FrmCF1HoA7sk=";
    //Cookie
//    @PostMapping("/auth/login")
//    public ResponseEntity<Object> login(@RequestBody Login login){
//        String accessToken = authService.signIn(login);
//        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
//                .domain("localhost") //todo 서버 환경에 따른 분리 필요
//                .path("/")
//                .httpOnly(true)
//                .secure(false)
//                .maxAge(Duration.ofDays(30))
//                .sameSite("Strict")
//                .build();
//        log.info("=====> cookie={}",cookie.toString());
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE,cookie.toString())
//                .build();
//    }
    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login){
        Long userPK = authService.signIn(login);

//        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        SecretKey secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(key));

        String jws = Jwts.builder().setSubject(userPK.toString()).signWith(secretKey).compact();

       return new SessionResponse(jws);

    }

}
