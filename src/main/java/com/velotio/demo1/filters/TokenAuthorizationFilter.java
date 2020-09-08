package com.velotio.demo1.filters;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthorizationFilter implements Filter {
    private String tokenSecret = "b1e66502-9a80-4913-b13b-2d1de7d0e090";
    private int tokenExpiry = 3600000;

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthorizationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        logger.info("Token auth filter called for " + httpRequest.getRequestURI());
        if (httpRequest.getRequestURI().equals("/zap_report")) {
            logger.info("Token auth filter activated");
            String bearerToken = httpRequest.getHeader("Authorization");

            if (!(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))) {
                httpResponse.setStatus(HttpStatus.FORBIDDEN.value());
                return;
            }

            String token = bearerToken.substring(7);
            String tokenErr = validate(token);

            if (tokenErr != null) {
                httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            String subject = getSubject(token);
            httpRequest.setAttribute("subject", subject);
        }

        filterChain.doFilter(httpRequest, httpResponse);
    }

    private String getSubject(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String validate(String token) {
        String err = null;

        try {
            Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token);
        } catch (SignatureException ex) {
            err = "Invalid JWT signature";
        } catch (MalformedJwtException ex) {
            err = "Invalid JWT token";
        } catch (ExpiredJwtException ex) {
            err = "Expired JWT token";
        } catch (UnsupportedJwtException ex) {
            err = "Unsupported JWT token";
        } catch (IllegalArgumentException ex) {

        }

        if (err != null) {
            logger.error(err);
        }

        return err;
    }
}
