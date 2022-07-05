package hh.com.uriharu.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import hh.com.uriharu.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Tokenprovider {
    private static final String SECRET_KEY ="NMA8JPctFuna59f5";

    public String create(UserEntity userEntity){
        //기한은 지금부터 1일
        Date expiryDate = Date.from(Instant.now().plus(1,ChronoUnit.DAYS));
        
 //JWT 생성
        return Jwts.builder()
        .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
        .setSubject(userEntity.getId())
        .setIssuer("demo app")
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .compact();
    }
 // Token  위조 여부 확인
    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser()
        .setSigningKey(SECRET_KEY)
        .parseClaimsJws(token)
        .getBody();

        return claims.getSubject();
        
    }
}
