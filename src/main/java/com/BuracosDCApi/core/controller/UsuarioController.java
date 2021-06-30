package com.BuracosDCApi.core.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.BuracosDCApi.core.generics.GenericRestController;
import com.BuracosDCApi.core.model.Credenciais;
import com.BuracosDCApi.core.model.NovoUsuario;
import com.BuracosDCApi.core.model.Papel;
import com.BuracosDCApi.core.model.Usuario;
import com.BuracosDCApi.core.security.UsuarioHelper;
import com.BuracosDCApi.core.service.PapelService;
import com.BuracosDCApi.core.service.UsarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController extends GenericRestController<Usuario, UsarioService> {

	@Autowired
	PapelService papelService;

	@Autowired
	UsuarioHelper usuarioHelper;

	@PostMapping("/{id}/papel")
	public @ResponseBody Usuario addPapel(@RequestBody Papel papel, @PathVariable("id") Integer id) throws Exception {
		Usuario usuario = service.findById(id).get();
		if (usuario.getPermissoes() == null)
			usuario.setPermissoes(new HashSet<Papel>());
		usuario.getPermissoes().add(papel);
		return this.service.update(usuario);
	}

	@DeleteMapping("/{id}/papel/{idPapel}")
	public @ResponseBody Usuario removePapel(@PathVariable("id") Integer id, @PathVariable("idPapel") Integer idPapel) throws Exception {
		Usuario usuario = service.findById(id).get();

		Optional<Papel> papel_ = papelService.findById(idPapel);
		Papel papel = papel_.get();

		usuario.getPermissoes().remove(papel);

		return service.update(usuario);
	}

	@PutMapping("/permissao")
	public @ResponseBody Boolean updatePermissoes(@RequestParam("usuarioId") Integer usuarioId,
			@RequestParam("papelId") Integer papelId) {
		Optional<Usuario> u = service.findById(usuarioId);
		Optional<Papel> p = papelService.findById(papelId);
		if (u.get().hasPapel(p.get())) {
			u.get().getPermissoes().remove(p.get());
		} else {
			u.get().getPermissoes().add(p.get());
		}
		service.salvarSemEncriptar(u.get());
		return true;
	}

	@PostMapping("/metodo/novoUsuario")
	public ResponseEntity<Usuario> gerarUsuarioPessoaFisica(@RequestBody NovoUsuario novoUsuario) throws Exception {
		return new ResponseEntity<Usuario>(this.service.gerarUsuario(novoUsuario.getPapel()), HttpStatus.OK);
	}

	@PutMapping("/metodo/resetarSenha/{id}")
	public void resetarSenha(@PathVariable("id") Integer id) {
		this.service.resetarSenha(id);
	}

	@PutMapping("/metodo/atualizarSenha")
	public Usuario atualizarSenha(@RequestBody Credenciais credenciais) throws Exception {
		return this.service.atualizarSenha(credenciais);
	}

	@PutMapping("/metodo/reativarUsuario/{id}")
	public void reativarUsuario(@PathVariable("id") Integer id) {
		this.service.reativarUsuario(id);
	}

	@GetMapping("/findByLogin/{login}")
	public Usuario findByLogin(@PathVariable("login") String login) {
		return this.service.findUsuarioByLogin(login);
	}

	@GetMapping("/findAllByLogin/{login}")
	public List<Usuario> findAllByLogin(@PathVariable("login") String login) {
		return this.service.findAllUsuarioByLogin(login);
	}

	@GetMapping("/isUsuarioLogadoChefe")
	public Boolean isUsuarioLogadoChefe() {
		return this.service.isUsuarioLogadoChefe();
	}

	@GetMapping("/findByPapel/{codigoPapel}")
	public List<Usuario> findByPapel(@PathVariable("codigoPapel") String codigoPapel) {

		Papel papel = papelService.findByCodigo(codigoPapel);
		List<Usuario> usuarios = this.service.findByPapel(papel.getId());
		return usuarios;
	}
}