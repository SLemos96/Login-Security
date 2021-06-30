package com.BuracosDCApi.core.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
@PropertySource("classpath:jwt.properties")
public class JwtTokenProvider {

	@Autowired
	protected UsuarioHelper usuarioHelper;

	/**
	 * Chave secreta, declarada no arquivo jwt.properties.
	 */
	@Value("${security.jwt.token.secret-key}")
	private String chaveSecreta;

	/**
	 * Tempo de duração da token, declarado no arquivo jwt.properties.
	 */
	@Value("${security.jwt.token.expire-length}")
	private long validadeMilisegundos;

	/**
	 * Objeto necessário para validar usuário.
	 */
	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * Converte o valor da chave secreta, em tempo de execução, pra um valor
	 * passível de ser usado pelo algoritmo de encriptação.
	 */
	@PostConstruct
	protected void init() {
		chaveSecreta = Base64.getEncoder().encodeToString(chaveSecreta.getBytes());
	}

	public JwtTokenProvider() {
		super();
	}

	public JwtTokenProvider(String chaveSecreta, long validadeMilisegundos) {
		this.chaveSecreta = chaveSecreta;
		this.validadeMilisegundos = validadeMilisegundos;
	}

	/**
	 * Cria uma token.
	 * 
	 * @param username       Nome do usuário que solicitou a token.
	 * @param permissoes     Permissões do usuário.
	 * @param primeiroAcesso Flag indicando se é o primeiro acesso do usuário.
	 * @return String contendo a token.
	 */
	public String createToken(String username, List<String> papeis, Boolean primeiroAcesso) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("papeis", papeis);
		claims.put("primeiroAcesso", primeiroAcesso);
		Date dataAtual = new Date();
		Date dataValidade = new Date(dataAtual.getTime() + validadeMilisegundos);
		return Jwts.builder().setClaims(claims).setIssuedAt(dataAtual).setExpiration(dataValidade)
				.signWith(SignatureAlgorithm.HS256, chaveSecreta).compact();
	}

	/**
	 * Cria uma nova token a partir de uma token existente.
	 * 
	 * @param token Token atual.
	 * @return String contendo nova token.
	 */
	public String refreshToken(String token) {
		Claims tokenClaims = Jwts.parser().setSigningKey(chaveSecreta).parseClaimsJws(token).getBody();
		Date dataAtual = new Date();
		Date dataValidade = new Date(dataAtual.getTime() + validadeMilisegundos);
		Claims novaClaims = Jwts.claims().setSubject(tokenClaims.getSubject());
		novaClaims.put("permissoes", tokenClaims.get("permissoes"));
		return Jwts.builder().setClaims(novaClaims).setIssuedAt(dataAtual).setExpiration(dataValidade)
				.signWith(SignatureAlgorithm.HS256, chaveSecreta).compact();

	}

	/**
	 * Método de autenticação necessário para o filter.
	 * 
	 * @param token Token do usuário.
	 * @return Objeto de autenticação.
	 */
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public Authentication getAuthentication(ServletRequest request) {
		String token = resolveToken((HttpServletRequest) request);
		if (token != null) {
			return getAuthentication(token);
		}
		return null;
	}

	public String getUsername(String token) {
		return Jwts.parser().setSigningKey(chaveSecreta).parseClaimsJws(token).getBody().getSubject();
	}

	/**
	 * Verifica se a requisicaouisição contém o cabeçalho necessário de autorização.
	 * 
	 * @param requisicao Requisição HTTP feita.
	 * @return String contendo a token enviada ou null.
	 */
	public String resolveToken(HttpServletRequest requisicao) {
		try {
			String bearerToken = requisicao.getHeader("Authorization");
			if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
				return bearerToken.substring(7, bearerToken.length());
			}
			return null;
		} catch (Exception e) {
//			User job = isJob();
//			if (job != null) {
//				return createToken(job.getLogin(), job.getPermissoesCodigo(), false);
//			} else {
				e.printStackTrace();
				return null;
//			}
		}
	}

	// Implementar caso precisar de Jobs/schedule
//	public Usuario isJob() {
//		Usuario job = usuarioHelper.getUsuarioLogado();
//		if (job != null && job.getLogin().equals("job")) {
//			return job;
//		}
//		return null;
//	}

	/**
	 * Valida uma token.
	 * 
	 * @param token Token a ser validada.
	 * @return Booleano indicando a validade da token.
	 */
	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(chaveSecreta).parseClaimsJws(token);
			if (claims.getBody().getExpiration().before(new Date())) {
				return false;
			}
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			throw new InsufficientAuthenticationException("Token expirada ou inválida");
		}
	}

}
