package com.rest.api.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rest.api.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtility implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(JwtUtility.class);
	
	private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	
	private String secretKey = "RestApi";
	
	//retreive username from jwt token
	public String getUsernameFromToken(String token)
	{
		String username;
		try {
			final Claims claims = getAllClaimsFromToken(token);
			username = claims.getSubject();
		}
		catch(Exception e) {
			username = null;
		}
		return username;
	}

	public String getIdFromToken(String token)
	{
		String id;
		try {
			final Claims claims = getAllClaimsFromToken(token);
			id = claims.getSubject();
		}
		catch(Exception e)
		{
			id = null;
		}
		return id;
	}
	
	
//	public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }

	//for rerieving any information from token we wil need the secret key
	private Claims getAllClaimsFromToken(String token) {
		
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}
	
	//retrieve expiration date from the jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimsFromToken(token, Claims::getExpiration);
	}

	
	private <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	//generate token for user
	public String generateToken(User user)
	{
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims,user.getUsername());
	}
	
	public String generate(User u) 
	{
        Claims claims = Jwts.claims().setSubject(u.getUsername());
        claims.put("userId", u.getId() + "");
        claims.put("role", u.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
	
	private String doGenerateToken(Map<String, Object> claims, String subject)
	{
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secretKey).compact();
	}
	
	//validate token
	public boolean validateToken(String authToken)
	{
		try
		{
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
			return true;
		}
		catch(SignatureException e) {
			logger.error("Invalid JWT signature: {}",e.getMessage());
		}
		catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}",e.getMessage());
		}
		catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}",e.getMessage());
		}
		catch(UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}",e.getMessage());
		}
		catch(IllegalArgumentException e) {
			logger.error("JWT claims string is empty:{}",e.getMessage());
		}
		
		return false;
	}
	
}
