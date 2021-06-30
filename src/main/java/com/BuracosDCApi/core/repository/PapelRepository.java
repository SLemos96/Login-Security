package com.BuracosDCApi.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.BuracosDCApi.core.generics.GenericRepository;
import com.BuracosDCApi.core.model.Papel;

public interface PapelRepository extends GenericRepository<Papel> {
	@Query(value = "SELECT id_papel, ativo, data_criacao, data_modificacao, codigo, descricao\n"
			+ "  FROM papel where codigo like 'ADMIN'\n" + "", nativeQuery = true)
	Papel findByAdmin();

	@Query("select p from Papel p where p.ativo = true")
	List<Papel> papeisAtivos();

	@Query("select p from Papel p where p.codigo like %?1% and p.ativo = true")
	List<Papel> findByCodigoLike(String codigo);

	Papel findByCodigoAndAtivoTrue(String codigo);

	@Query("SELECT DISTINCT(papeis) from Usuario u " + "JOIN u.permissoes papeis " + "WHERE u.id = ?1 "
			+ "AND papeis.ativo = true")
	List<Papel> findByUsuario(int idUsuario);
}
