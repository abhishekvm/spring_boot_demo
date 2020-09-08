package com.velotio.demo1.services;

import com.velotio.demo1.domains.Organization;
import com.velotio.demo1.domains.OrganizationRepository;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("tokenService")
public class TokenService {

    @Autowired
    OrganizationRepository organizationRepository;

    private String tokenSecret = "b1e66502-9a80-4913-b13b-2d1de7d0e090";
    private int tokenExpiry = 3600000;

    Logger logger = LoggerFactory.getLogger(TokenService.class);

    public String generate(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenExpiry);
        Organization organization = organizationRepository.findByName(email.split("@")[1]);

        return Jwts.builder()
                .setSubject(email + "-" + organization.getId())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    public String getSubject(String token) {
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
