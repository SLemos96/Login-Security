package com.BuracosDCApi.exemplo;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BuracosDCApi.core.generics.GenericService;
import com.BuracosDCApi.core.service.UsuarioService;

@Service
@Transactional
public class ExemploService extends GenericService<ModelDeExemplo, ExemploRepository> {

	// caso vc queira injetar manualmente alguma classe é só fazer da seguinte forma: 
	@Autowired
	private UsuarioService usuService;
	
	// e ai vc ja pode utiliza-lo
	public void exUtilizacao() {
		this.usuService.count();
	}
	
	
	// O segredo aqui é só utilizar os campos de genericService que são
	// automagicamente injetados no service

	// ex:

	public ModelDeExemplo getModels() {
		return this.repository.findModels();
	}
	
	//vc pode passar parametros se quiser
	// ... na true não tem muito segredo aqui

	
	// Na pratica aqui é o unico lugar que vc pode SE QUISER criar um metodo private ou protected... em qualquer outro canto da aplicação da ruim
	
}
