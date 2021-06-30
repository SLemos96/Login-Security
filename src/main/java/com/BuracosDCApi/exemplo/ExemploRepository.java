package com.BuracosDCApi.exemplo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.BuracosDCApi.core.generics.GenericRepository;

public interface ExemploRepository extends GenericRepository<ModelDeExemplo> {

	// por default ja tem varios metodos implementados no genericRepository,
	// então vc só vai implementar algo especifico que não tenha la

	@Query("select m from ModelDeExemplo m")
	public ModelDeExemplo findModels();

	@Query(value = "select * from teste.model_de_exemplo", nativeQuery = true) // caso vc
	public ModelDeExemplo findModelsNativo();

	// as duas query acima fazem a mesma coisa de formas diferentes, existem coisas
	// que não da pra fazer com hql (a forma de cima)
	// mas em suma hql é beeeem mais simples que sql ... pelo menos é mais
	// rapido/intuitivo
	// note também que a sintaxe é meio diferente, select * em hql não funciona, e
	// vc passa o nome da classe, enquanto native query
	// é passado o nome da schema.tabela

	@Query("select m from ModelDeExemplo m where ativo=?1")
	public List<ModelDeExemplo> findModelsNativo(boolean ativo);
	// é possivel passar parametro de 2 formas
	// ?1 = primeiro parametro, ?2 = segundo parametro ...
	// select m from ModelDeExemplo where ativo=:ativo

	// voce pode retornar basicamente qualquer coisa, desde que consiga castar
	// corretamente
	// como foi retornado uma lista acima
	// só cuidado com a native query, pois obviamente vc pode dar um select
	// <campoEspecifico> from ... e retornar só alguns campos

	// AH E INFORMAÇÂO DE EXTREEEMA IMPORTANCIA
	// Se tu colocar private ou protected em qualquer metodo aqui da uma merda do
	// kct
	// então SEMPRE deixa eles assim public

}