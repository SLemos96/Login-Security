package com.BuracosDCApi.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.BuracosDCApi.core.model.HistoricoSenha;

public interface HistoricoSenhaRepository extends JpaRepository<HistoricoSenha, Integer> {

	@Query(value = "SELECT * FROM public.historico_senha WHERE id_usuario = ?1 ORDER BY id DESC LIMIT 3", nativeQuery = true)
	List<HistoricoSenha> findByUsuario(Integer idUsuario);

}
