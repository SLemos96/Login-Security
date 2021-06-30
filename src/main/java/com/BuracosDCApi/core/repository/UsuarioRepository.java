package com.BuracosDCApi.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.BuracosDCApi.core.generics.GenericRepository;
import com.BuracosDCApi.core.model.Papel;
import com.BuracosDCApi.core.model.Usuario;

@Repository
public interface UsuarioRepository extends GenericRepository<Usuario> {

	@Query(value = "select * from Usuario where login = ?1 and ativo = true", nativeQuery = true)
	public Usuario findByLogin(String login);

	@Query("select u from Usuario u where u.login like %?1% and u.ativo = true")
	List<Usuario> findAllByLogin(String login);

	@Query(value = "select * from Usuario u where u.email like ?1", nativeQuery = true)
	public Usuario findUsuarioByEmail(String email);

	@Query(value = "select * from Usuario u where u.codigo_recuperar_senha like ?1", nativeQuery = true)
	public Usuario findUsuarioByCodigo(String codigo);

	@Query("SELECT DISTINCT(u) FROM Usuario u JOIN u.permissoes p where p.id = ?1 and u.ativo = true")
	public List<Usuario> findByPapel(Integer papelId);

	@Query("SELECT count(u) > 0 " + "FROM Usuario u JOIN u.permissoes p " + "WHERE u.id = :idUsuario "
			+ "AND p.codigo IN :codigoPapeis " + "AND p.ativo = true")
	public Boolean hasPapeis(Integer idUsuario, List<String> codigoPapeis);

	@Query(value = "SELECT DISTINCT(u) FROM Usuario u " + "JOIN u.permissoes p " + "WHERE p IN :papeis "
			+ "AND u.ativo = true")
	List<Usuario> findAllUsuarioComPapeis(List<Papel> papeis);

}
