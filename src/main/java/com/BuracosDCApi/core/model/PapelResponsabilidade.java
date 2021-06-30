package com.BuracosDCApi.core.model;

public enum PapelResponsabilidade {
	CHEFE("Chefe"),
	GERENTE("Gerente"),
	USUARIO("Usuário"),
	SECRETARIA("Secretária"),
	ANALISTA_MB("Analista do Laboratório Micro-Biológico"),
	ANALISTA_FQ("Analista do Laboratório Físico-Químico"),
	ANALISTA_CP("Analista do Controle em Processo"),
	RESPONSAVEL("Responsável"),
	EXECUTANTE("Executante");

	private String name;
	
	private PapelResponsabilidade(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}