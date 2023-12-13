package com.example.cafemanagementsystem.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtUtil {

    // ***kullanıcı kimlik doğrulama ve yetkilendirme işlemleri***


    //Güvenlik açısından konfigürasyon dosyalarından almak gerekir
    private  String secret = "yunus";


    public String extractUserName(String token) {
        return extractClamis(token,Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClamis(token,Claims::getExpiration);
    }

    public <T> T  extractClamis(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return  claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }


//Verilen JWT'nin geçerlilik süresinin dolup dolmadığını kontrol eder.
    private  Boolean isTokenExpired(String token){
        return  extractExpiration(token).before(new Date());
    }

    //Kullanıcı adı ve rol bilgilerini içeren bir JWT oluşturur. Rol bilgisi, kullanıcının sistemdeki rollerini belirtir.
    public  String generateToken(String username , String role) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("role",role);
       return createToken(claims,username);
    }

    private  String createToken(Map<String, Object> claims,String subject) {

        return  Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10))
                .signWith(SignatureAlgorithm.HS256,secret).compact();
    }

    //Verilen JWT'nin geçerliliğini doğrular. Kullanıcı adını çıkarır ve token süresinin dolup dolmadığını kontrol eder.
    public  Boolean validateToken(String token, UserDetails userDetails){
        final  String username= extractUserName(token);
        return  (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }



}
