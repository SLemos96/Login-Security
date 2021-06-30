package com.BuracosDCApi.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.BuracosDCApi.core.model.Usuario;
import com.BuracosDCApi.core.repository.UsuarioRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

//	public CustomUserDetailsService(UserRepository usuarioRepository) {
//		this.usuarioRepository = usuarioRepository;
//	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = this.usuarioRepository.findByLogin(username);
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário " + username + " não encontrado");
		} else {
			return usuario;
		}
	}
}
