package com.BuracosDCApi.exemplo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.BuracosDCApi.core.generics.GenericRestController;

@RestController
@RequestMapping("/endpoint") // lembrando que para acessar a aplicação o contexto é
								// localhost:8080/burdcapi/<endpoint>
public class ExemploController extends GenericRestController<ModelDeExemplo, ExemploService> {
// vc pode mudar o contexto geral da aplicação ou a porta no application.properties prop server.servlet.context-path

	// Muito parecido com o service
	// ... bastante mesmo

	@GetMapping("/pegarModel") // com isso fecha esse caminho como localhost:8080/burdcapi/endpoint/pegarModel
	@ResponseBody
	public ModelDeExemplo getModelExemplo() {
		return this.service.getModels();
	}

	// só para vc saber como faz pra passar parametros pela url
//	@GetMapping("/findLocalizacoes/{idSetor}")
//	public List<Localizacao> findLocalizacaoBySetorId(@PathVariable("idSetor") Integer idSetor, @RequestParam("descricao") String descricao){
//		return this.service.findLocalizacaoBySetorId(idSetor);
//	}

	// perceba que da pra passar dados de 2 formas
	// requestParam que vem pelo json que vc manda do front end
	// pathVariable vem pela url ex: localhost:8080/burdcapi/endpoint/model/5

}
