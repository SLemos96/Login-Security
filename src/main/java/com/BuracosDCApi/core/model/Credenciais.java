package com.BuracosDCApi.core.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class Credenciais implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(notes = "Login do usuário.")
	private String login;

	@ApiModelProperty(notes = "Senha do usuário.")
	private String senha;
	
	@ApiModelProperty(notes = "Login anterior do usuário (usado para validação em troca de senhas).")
	private String loginAnterior;
	
	public Credenciais() {
		super();
	}

	public Credenciais(String login, String senha) {
		super();
		this.login = login;
		this.senha = senha;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getLoginAnterior() {
		return loginAnterior;
	}

	public void setLoginAnterior(String loginAnterior) {
		this.loginAnterior = loginAnterior;
	}

}
