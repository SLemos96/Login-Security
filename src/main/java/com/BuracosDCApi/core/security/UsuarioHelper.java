package com.BuracosDCApi.core.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.BuracosDCApi.core.model.Papel;
import com.BuracosDCApi.core.model.Usuario;


@Component
public class UsuarioHelper {

	public Usuario getUsuarioLogado() {
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public boolean hasRole(String role) {
		return hasRole(role, getUsuarioLogado());
	}

	public boolean hasRole(String role, Usuario usuario) {
		if (usuario != null) {
			for (Papel p : usuario.getPermissoes()) {
				if (p.getCodigo().equals(role)) {
					return true;
				}
			}
		}
		return false;
	}

}
