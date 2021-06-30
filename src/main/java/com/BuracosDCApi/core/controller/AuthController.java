package com.BuracosDCApi.core.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.SessionException;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BuracosDCApi.core.model.Credenciais;
import com.BuracosDCApi.core.model.Papel;
import com.BuracosDCApi.core.model.Usuario;
import com.BuracosDCApi.core.security.AuthenticationHelper;
import com.BuracosDCApi.core.security.JwtTokenProvider;
import com.BuracosDCApi.core.service.UsarioService;
import com.BuracosDCApi.core.util.LoggedUserStore;


@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

	private final Logger LOGGER = Logger.getLogger(AuthController.class.getName());

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Autowired
	UsarioService usuarioService;
	
	@Autowired
	LoggedUserStore userStore;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Credenciais credenciais) {
		Usuario usuario = usuarioService.findUsuarioByLogin(credenciais.getLogin());
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário " + credenciais.getLogin() + " não encontrado");
		} else {
			try {
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usuario, credenciais));
				LOGGER.info("Usuário: [" + credenciais.getLogin() + "] - Operação: [LOGIN]");
				String username = credenciais.getLogin();
				String token = jwtTokenProvider.createToken(credenciais.getLogin(), usuario.getPermissoesCodigo(),
						usuario.getPrimeiroAcesso());
				// se for primeiro acesso, não entra na lista de usuários logados
				if (usuario.getPrimeiroAcesso()) {
					return new ResponseEntity<String>(token, HttpStatus.OK);
				}
				// se usuário não está logado, adiciona na lista de usuários logados
				if (!userStore.isLoggedIn(credenciais.getLogin())) {
					userStore.addUser(username, token);
					return new ResponseEntity<String>(token, HttpStatus.OK);
				} else {
					// se usuário está logado, retorna resposta normalmente caso sejam usuários do sistema
					if (username.equals("frequencia")|| username.equals("job")) {
						return new ResponseEntity<String>(token, HttpStatus.OK);
					}
					// se usuário já consta na lista de usuários logados com o mesmo token, retorna resposta normalmente
					if (userStore.validateToken(username, token)) {
						return new ResponseEntity<String>(token, HttpStatus.OK);
					} else {
						// se usuário já consta na lista de usuários com token diferente, retorna erro
						return new ResponseEntity<String>(token, HttpStatus.ALREADY_REPORTED);
					}
				}
			} catch (SessionException e) {
				throw e;
			} catch (AuthenticationException e) {
				LOGGER.info("Usuário: [" + credenciais.getLogin() + "] - Operação: [TENTATIVA DE ACESSO]");
				throw new BadCredentialsException("Login e/ou senha inválidos");
			} catch (IllegalArgumentException e) {
				LOGGER.info("Usuário: [" + credenciais.getLogin() + "] - Operação: [TENTATIVA DE ACESSO (USUÁRIO BLOQUEADO)]");
				return new ResponseEntity<String>("Acesso bloqueado. Favor contactar responsável.", HttpStatus.LOCKED);
			}
		}

	}

	/**
	 * Retorna as permissoes do usuario
	 */
	@GetMapping("/permissoes")
	public Set<Papel> permissoes(HttpServletRequest request) {
		String token = jwtTokenProvider.resolveToken(request);
		String username = jwtTokenProvider.getUsername(token);
		return usuarioService.findUsuarioByLogin(username).getPermissoes();
	}

	/**
	 * Recarrega token.
	 * 
	 * @param request Requisição HTTP.
	 * @return Token atualizada caso token atual não esteja expirada.
	 */
	@GetMapping("/refresh")
	public String refresh(HttpServletRequest request) {
		String token = jwtTokenProvider.resolveToken(request);
		if (token != null && jwtTokenProvider.validateToken(token)) {
			return jwtTokenProvider.refreshToken(token);
		}
		return "false";
	}

	/**
	 * Validar credenciais informadas
	 * 
	 * @param credenciais Login e senha
	 * @return Booleano indicando validade
	 */
	@PostMapping("/checarValidade")
	public Boolean checarSenha(@RequestBody Credenciais credenciais) {
		Usuario usuario = usuarioService.findUsuarioByLogin(credenciais.getLogin());
		if (usuario != null) {
			return AuthenticationHelper.verificarSenha(usuario, credenciais.getSenha());
		}
		return false;
	}

	/**
	 * Verifica se existe apenas uma sessão referente ao usuário
	 * 
	 * @param user Nome do usuário
	 * @param jwt  Token
	 * @return
	 */
	@GetMapping("/verificarSessao")
	public Boolean isUsuarioLogado(HttpServletRequest request) {
		String token = jwtTokenProvider.resolveToken(request);
		String username = jwtTokenProvider.getUsername(token);
		return userStore.validateToken(username, token);
	}

	/**
	 * Atualiza a sessão sobrescrevendo o token já definido para o usuário
	 * 
	 * @param user Nome do usuário
	 * @param jwt  Token
	 */
	@PostMapping("/atualizarSessao")
	public Usuario atualizarTokenUsuarioLogado(@RequestBody String token) {
		String username = jwtTokenProvider.getUsername(token);
		userStore.addUser(username, token);
		return usuarioService.findUsuarioByLogin(username);
	}

	@GetMapping("/removerUsuariosLogados")
	public void limparUsuariosLogados() {
		userStore = new LoggedUserStore();
	}

	@PostMapping("/logout")
	public void logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String token = jwtTokenProvider.resolveToken(request);
		String username = jwtTokenProvider.getUsername(token);
		if (session != null) {
			session.invalidate();
			LOGGER.info("Usuário: [" + username + "] - Operação: [LOGOUT]");
			this.userStore.removeUser(username);
		}
	}
}