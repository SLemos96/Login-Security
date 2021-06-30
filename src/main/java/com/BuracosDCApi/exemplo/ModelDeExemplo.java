package com.BuracosDCApi.exemplo;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.BuracosDCApi.core.generics.GenericEntity;

@Entity
@Table(name = "nomeDaTabela")//, schema = "teste")  // o schema é opcional caso queira organizar as entidades
// vou deixar o schema comentado mas caso vc queria utilizar é o seguinte:
// vc vai ter que criar o schema antes de rodar o projeto
// no dbeaver é bem simples e intuitivo, clicou no banco > schemas > create new schema
// no psql é "\c buracodc" > "create schema teste;"(n esquece o ; no create schema)
@AttributeOverride(name = "id", column = @Column(name = "id_nome_tabela"))
public class ModelDeExemplo extends GenericEntity {

	private static final long serialVersionUID = 1L;

	// abaixo aqui vem os campos da tabela

	@NotNull(message = "Sigla do Setor vazia.")
	@NotBlank(message = "Sigla do Setor vazia.")
	@Column(unique = true)
	private String sigla;

	// um exemplo de one to many pra não ficar perdido
//	@OneToMany(fetch = FetchType.LAZY)
//	@JoinColumn(name = "id_setor_localizacao")  // esse campo deve estar presente na tabela localização
//	private List<Localizacao> listLocalizacao;

	// um exemplo de uso de enum
//	@NotNull(message = "Tipo do Setor vazio.")
//	@Enumerated(EnumType.STRING)
//	private TipoSetor tipoSetor;
}
