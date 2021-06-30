package com.BuracosDCApi.core.security;

import java.util.Collection;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.BuracosDCApi.core.model.Credenciais;
import com.BuracosDCApi.core.model.Usuario;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

	/**
	 * Autentica as credenciais informadas, retornando exceções de acordo.
	 */
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		Credenciais credenciais = (Credenciais) auth.getCredentials();
		Usuario usuario = (Usuario) auth.getPrincipal();
		if (usuario != null) {
			if (AuthenticationHelper.verificarSenha(usuario, credenciais.getSenha())) {
				Collection<? extends GrantedAuthority> authorities = usuario.getPermissoes();
				return new UsernamePasswordAuthenticationToken(usuario, auth.getCredentials(), authorities);
			} else {
				throw new BadCredentialsException("Login e/ou senha inválidos.");
			}
		}
		throw new UsernameNotFoundException("Login e/ou senha inválidos.");
	}

	/**
	 * Retorna uma flag indicando se o tipo de objeto passado é autenticável.
	 */
	@Override
	public boolean supports(Class<?> auth) {
		return auth.equals(UsernamePasswordAuthenticationToken.class);
	}
}