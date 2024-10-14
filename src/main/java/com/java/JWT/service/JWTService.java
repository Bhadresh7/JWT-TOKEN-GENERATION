package com.java.JWT.service;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

	private String secretKey="";

	//generating a secretkey
	public JWTService() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator= KeyGenerator.getInstance("HmacSHA256");
		SecretKey sk= keyGenerator.generateKey(); // here the SecretKey is a built-in type


		//here the secretKey is the variable 
		secretKey=Base64.getEncoder().encodeToString(sk.getEncoded());
	}

	public String generateToken(String username) {
		Map<String,Object>claims= new HashMap<>();

		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(username)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis()+60*60*1000))
				.and()
				.signWith(getkey())
				.compact();
	}

	//converting the String key to byte cuz hmacShaKeyFor() accepts only bytes
	private SecretKey getkey() {
		byte[] keybyte=Decoders.BASE64.decode(secretKey); 
		return Keys.hmacShaKeyFor(keybyte);
	}

	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	private  <T> T extractClaim(String token, Function<Claims, T>claimResolver) {
		final Claims claims=extractAllClaims(token);
		return  claimResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		// TODO Auto-generated method stub
		return Jwts.parser()
				.verifyWith(getkey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String userName= extractUserName(token);
		return userName.equals(userDetails.getUsername())&& !isTokenExpired(token);
	}

	
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
		
	}
	
	private Date extractExpiration (String token) {
		return extractClaim(token,Claims::getExpiration);
	}
	
	
	
}
