package com.BuracosDCApi.core.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

public class LoggedUserStore {

	public class SessaoInfo {

		private String token;

		@DateTimeFormat(pattern = "yyyy-MM-dd")
		@Temporal(TemporalType.TIMESTAMP)
		private Date dataHoraAcesso = new Date();

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public Date getDataHoraAcesso() {
			return dataHoraAcesso;
		}

		public void setDataHoraAcesso(Date dataHoraAcesso) {
			this.dataHoraAcesso = dataHoraAcesso;
		}

	}

	/**
	 * Lista de usuários no formato login token
	 */
	private Map<String, SessaoInfo> users = new HashMap<String, SessaoInfo>();

	public void addUser(String user, String jwt) {
		SessaoInfo info = new SessaoInfo();
		// remover ocorrências de "=" que surgem esporadicamente no token
		jwt = jwt.replace("=", "");
		info.setToken(jwt);
		users.put(user, info);
	}

	public void removeUser(String user) {
		users.remove(user);
	}

	/**
	 * Verifica se o usuário já está inserido na lista com a respectiva token
	 * 
	 * @param user Nome do usuário
	 * @param jwt  Token
	 * @return Verdadeiro se o usuário já estiver logado com a token, falso caso
	 *         contrário
	 */
	public Boolean isLoggedIn(String user) {
		return users.containsKey(user);
	}
	
	public Boolean validateToken(String user, String jwt) {
		SessaoInfo info = users.get(user);
		try {
			return info.getToken().equals(jwt);
		} catch (NullPointerException e) { // se não for encontrado (retorno null no map), retorna false
			return false;
		}
	}
	
	public String toString() {
		String out = "";
		for (Entry<String, SessaoInfo> entry : users.entrySet()) {
			out += "Usuário: " + entry.getKey() + ", Info: Token " + entry.getValue().getToken() + ", Data: " + entry.getValue().getDataHoraAcesso();
		}
		return out;
	}

	public Map<String, SessaoInfo> getUsers() {
		return users;
	}

	public void setUsers(Map<String, SessaoInfo> users) {
		this.users = users;
	}
}