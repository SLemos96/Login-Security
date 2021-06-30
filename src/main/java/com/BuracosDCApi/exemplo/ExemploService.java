package com.BuracosDCApi.exemplo;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.BuracosDCApi.core.generics.GenericService;

@Service
@Transactional
public class ExemploService extends GenericService<ModelDeExemplo, ExemploRepository> {

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
