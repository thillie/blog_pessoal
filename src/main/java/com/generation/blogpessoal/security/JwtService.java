package com.generation.blogpessoal.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

//o objetivo da classe é confirmar o token
@Component
public class JwtService {
	
	//final = possui um valor constante que não pode ser alterado
	//token é composto pelo email, data de expiração
	public static final String SECRET = "3db18a004936c7e64685f03ac2f806811afed2cf123bee812d7511a2081a531f";
	
	//assinatura do token
	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);//encodando a secret que á a chave 
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	//claims é a classe do java que carrega declarações e informações que estão dentro do token
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignKey()).build()
				.parseClaimsJws(token).getBody();
	}
	
	//assinatura extraída que torna entendivel
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	//parte que econtra o email(usuario)
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	//data que o token expira
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	//verifica se o token ainda está dentro do prazo de validade
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	//validar se o usuário que foi extraído do token condiz com o usuário que a UseDetails tem e se está dentro da data de validade do token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	//calcular o tempo de validade do token, formar o claim com as informações do token
	private String createToken(Map<String, Object> claims, String userName) {
		return Jwts.builder()
					.setClaims(claims)
					.setSubject(userName)
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
					.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}
	
	//gerar o token puxando os claims formados no método anterior 
	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userName);
	}
	
}
