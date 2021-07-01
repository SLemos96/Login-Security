package com.BuracosDCApi.core.model;

public enum PapelResponsabilidade {
	CHEFE("Chefe"),
	GERENTE("Gerente"),
	USUARIO("Usuário"),
	SECRETARIA("Secretária"),
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