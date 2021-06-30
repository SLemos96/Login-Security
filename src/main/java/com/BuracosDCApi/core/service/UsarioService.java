package com.BuracosDCApi.core.service;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.BuracosDCApi.core.generics.GenericService;
import com.BuracosDCApi.core.model.Credenciais;
import com.BuracosDCApi.core.model.HistoricoSenha;
import com.BuracosDCApi.core.model.Papel;
import com.BuracosDCApi.core.model.PapelResponsabilidade;
import com.BuracosDCApi.core.model.Usuario;
import com.BuracosDCApi.core.repository.HistoricoSenhaRepository;
import com.BuracosDCApi.core.repository.PapelRepository;
import com.BuracosDCApi.core.repository.UsuarioRepository;
import com.BuracosDCApi.core.security.AuthenticationHelper;
import com.BuracosDCApi.core.security.UsuarioHelper;


@Service
@Transactional
public class UsarioService extends GenericService<Usuario, UsuarioRepository> {

	@Autowired
	UsuarioHelper usuarioHelper;

	@Autowired
	PapelRepository papelRepository;
	
	@Autowired
	HistoricoSenhaRepository historicoSenhaRepository;

	@Override
	public List<Usuario> findAll() {
		return super.findAll();
	}

	@Override
	public void validate(Usuario entity) throws Exception {
		String senha = entity.getSenha();
		if (senha == null) {
			throw new Exception("senha nula");
		}
		if (senha.length() < 8) {
			throw new Exception("tamanho insuficiente de senha");
		}
		if (entity.getSenha().contains(entity.getLogin())) {
			throw new Exception("senha contem parte do login");
		}
		Pattern regexChars = Pattern.compile("[#!@$%&*_\\-\\+:;=|()\\{}\\[\\],\\.\\?]");
		if (!regexChars.matcher(senha).find()) {
			throw new Exception("senha sem caracteres especiais");
		}
		Pattern regexLettersNums = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])");
		if (!regexLettersNums.matcher(senha).lookingAt()) {
			throw new Exception("senha não atende a regra");
		}
	}

	/**
	 * Insere o usuário no banco, encriptando antes sua senha.
	 */
	@Override
	public Usuario save(Usuario usuario) throws Exception {
		validate(usuario);
		String senha = usuario.getSenha();
		usuario.setSenha(BCrypt.hashpw(senha, BCrypt.gensalt(12)));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 6);
		usuario.setDataExpiracaoSenha(cal.getTime());
		usuario.setNotificadoExpiracaoSenha(false);

		Set<Papel> permissoes = new HashSet<Papel> (usuario.getPermissoes());
		Papel papelUsuarioComum = papelRepository.findByCodigoAndAtivoTrue("USUARIO_COMUM");
		if(!permissoes.contains(papelUsuarioComum)) {
			permissoes.add(papelUsuarioComum);
			usuario.setPermissoes(permissoes);
		}

		Usuario usuarioSalvo = repository.save(usuario);
		if (usuarioSalvo != null) {
			// boa sorte pra implementar isso
			//EmailHelper.enviarEmailNovoUsuario(usuario.getLogin(), senha);
		}
		return usuarioSalvo;
	}

	public void reativarUsuario(Integer id) {
		Usuario usuario = repository.findByIdIgnoreAtivo(id).get();
		usuario.setAtivo(true);
		this.repository.save(usuario);
	}


	public Usuario gerarUsuario(Papel papel) throws Exception {
		Usuario usuario = new Usuario();

		if(papel == null) {
			throw new Exception("Usuario sem papel");
		}

		String login = usuario.getEmail().split("@")[0];
		if (this.repository.findByLogin(login) != null) {
			login += (int) (Math.random() * 100);
		}
		String senha = AuthenticationHelper.gerarNovaSenha();
		Set<Papel> listPapel = new HashSet<Papel>();
		listPapel.add(papel);
		usuario.setLogin(login);
		usuario.setSenha(senha);
		usuario.setPermissoes(listPapel);
		return this.save(usuario);
	}

	public void resetarSenha(Integer id) {
		Usuario usuario = repository.findById(id).get();
		String novaSenha = AuthenticationHelper.gerarNovaSenha();
		System.err.println(novaSenha);
		usuario.setSenha(BCrypt.hashpw(novaSenha, BCrypt.gensalt(12)));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 6);
		usuario.setDataExpiracaoSenha(cal.getTime());
		Usuario usuarioSalvo = this.repository.save(usuario);
		if (usuarioSalvo != null) {
//			EmailHelper.enviarEmailResetSenha(usuario.getPessoa(), novaSenha, urlUtil.getUrl());
		}
	}

	/**
	 * Atualiza a senha de um usuário a partir de suas credenciais.
	 * 
	 */
	public Usuario atualizarSenha(Credenciais credenciais) throws Exception {
		Usuario usuarioCredenciais = this.repository.findByLogin(credenciais.getLogin()); 
		// se o usuario logado for diferente das credenciais informadas e estiver usando as credenciais de outro usuario
		if (usuarioCredenciais != null && !usuarioHelper.getUsuarioLogado().getId().equals(usuarioCredenciais.getId())) {
			throw new Exception("Já existe um usuário cadastrado com esse login no sistema.");
		}
		if (credenciais.getLogin().contains(" ") || credenciais.getLogin().isEmpty()) {
			throw new Exception("Login com espaços em branco ou está vazio.");
		}
		Usuario usuario = null;
		if (credenciais.getLoginAnterior() != null) {
			usuario = this.repository.findByLogin(credenciais.getLoginAnterior());
		} else {
			usuario = this.repository.findByLogin(credenciais.getLogin());
		}
		List<HistoricoSenha> historicoSenha = historicoSenhaRepository.findByUsuario(usuario.getId());
		for (HistoricoSenha hs : historicoSenha) {
			if (AuthenticationHelper.verificarSenha(credenciais.getSenha(), hs.getSenha())) {
				throw new Exception("A nova senha é igual a uma das últimas três senhas utilizadas");
			}
		}
		HistoricoSenha hr = new HistoricoSenha();
		hr.setUsuario(usuario);
		hr.setSenha(usuario.getSenha());
		historicoSenhaRepository.save(hr);
		usuario.setLogin(credenciais.getLogin());
		usuario.setSenha(BCrypt.hashpw(credenciais.getSenha(), BCrypt.gensalt(12)));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 6);
		usuario.setDataExpiracaoSenha(cal.getTime());
		return this.repository.save(usuario);
	}

	public Usuario salvarSemEncriptar(Usuario usuario) {
		return this.repository.save(usuario);
	}

	public Usuario findUsuarioByLogin(String login) {
		return this.repository.findByLogin(login);
	}


	public List<Usuario> findAllUsuarioByLogin(String login) {
		return this.repository.findAllByLogin(login);
	}

	public List<Usuario> findByPapel(Integer papelId) {
		return repository.findByPapel(papelId);
	}

	public Boolean isUsuarioLogadoChefe() {
		return usuarioHelper.getUsuarioLogado().hasResponsabilidade(PapelResponsabilidade.CHEFE);
	}
}
