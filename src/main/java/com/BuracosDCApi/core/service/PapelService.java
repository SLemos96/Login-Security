package com.BuracosDCApi.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.BuracosDCApi.core.generics.GenericService;
import com.BuracosDCApi.core.model.Papel;
import com.BuracosDCApi.core.repository.PapelRepository;

@Service
@Transactional
public class PapelService extends GenericService<Papel, PapelRepository> {

	private Map<String, Papel> mapaDePapeis;

	public Papel findByCodigo(String codigo) {
		return repository.findByCodigoAndAtivoTrue(codigo);
	}

	public List<Papel> findBy(String fieldName, String value) {
		switch (fieldName) {
		case "codigo":
			return repository.findByCodigoLike(value);
		default:
			return repository.findAll();
		}
	}

	public Map<String, Papel> getMapaDePapeis() {
		if (mapaDePapeis == null)
			initMapaDePapeis();
		return mapaDePapeis;
	}

	/**
	 * Carregar papeis para serem utilizados pelo filtro
	 */
	synchronized public void initMapaDePapeis() {
		mapaDePapeis = new HashMap<String, Papel>();
		for (Papel papel : repository.findAll()) {
			mapaDePapeis.put(papel.getCodigo(), papel);
		}
	}

	@Override
	public Papel save(Papel obj) throws Exception {
		Papel papel = super.save(obj);
		initMapaDePapeis();
		return papel;
	}

	@Override
	public Papel update(Papel obj) throws Exception {
		Papel papel = super.update(obj);
		initMapaDePapeis();
		return papel;
	}

	@Override
	public void validate(Papel papel) throws Exception {
		if (papel.getDescricao() == null || papel.getCodigo() == null)
			throw new Exception("Papel do usuário inválido");
	}
}
