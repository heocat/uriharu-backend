package hh.com.uriharu.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    @Autowired
    private Tokenprovider tokenprovider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            //요청에서 token 가져옴
            String token = parseBearerToken(request);
            log.info("Filter is running....");
            //토큰을 검사. JWT 이므로 인가 서버에 요청하지 않고도 검증가능

            if (token != null && !token.equalsIgnoreCase("null")) {
                //user id 가져오기. 위조된 경우 예외처리됨
                String userId = tokenprovider.validateAndGetUserId(token);
                log.info("Authenticated user Id : " + userId);
                //인증 완료. SEcurityContextHolder 에 등록해야 인증된 사용자라고 생각한다.
                AbstractAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken( 
                userId, // 인증된 사용자의 정보. 문자열이 아니어도 아무것이나 넣을 수 있다. 보통 UserDetails 오브젝트를 삽입(principal)
                 null,
                 AuthorityUtils.NO_AUTHORITIES);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //SecurityContext에 사용자 인증 정보를 등록한다.
                //1.SecurityContextHolder 에 빈 컨텍스트 생성
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                //2.securityContext에 사용자 인증 정보 등록
                securityContext.setAuthentication(authentication);
                //3.SecurityContextHolder에 생성된 컨텍스트를 다시 set 해준다
                SecurityContextHolder.setContext(securityContext);
            }
            
        } catch (Exception ex) {
            logger.error("Could new set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        //http 요청의 헤더를 파싱해 bearer 토큰을 리턴함
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
