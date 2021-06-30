package com.BuracosDCApi.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PostLoad;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.BuracosDCApi.core.generics.GenericEntity;
import com.BuracosDCApi.util.CustomDateDeserializer;
import com.BuracosDCApi.util.DateFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
@Audited
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@AttributeOverride(name = "id", column = @Column(name = "id_usuario"))
public class Usuario extends GenericEntity implements UserDetails {

	private static final long serialVersionUID = 1L;

	/**
	 * Login do usuário no sistema
	 */
	@NotBlank
	@Column(unique = true)
	private String login;

	/**
	 * Senha de acesso do usuário no sistema
	 */
	private String senha;

	@Email
	@NotBlank
	@Column(unique = true, nullable = false)
	private String email;

	/**
	 * Coleção de objetos da Classe Permissão que representa as credenciais do
	 * usuário no sistema
	 */
	@Audited
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "permissao", schema = "public", joinColumns = @JoinColumn(name = "id_usuario"), inverseJoinColumns = @JoinColumn(name = "id_papel"))
	private Set<Papel> permissoes;

	@JsonDeserialize(using = CustomDateDeserializer.class)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = DateFormat.DATE, timezone = DateFormat.TIMEZONE)
	private Date dataExpiracaoSenha;

	private Boolean notificadoExpiracaoSenha = false;

	public Usuario() {
	}

	public Usuario(@NotBlank String login) {
		this.login = login;
	}

	@Override
	public String getUsername() {
		return this.login;
	}

	@JsonIgnore
	public boolean hasPapel(Papel papel) {
		return permissoes.contains(papel);
	}

	@JsonIgnore
	public boolean hasPapel(String papel) {
		boolean encontrou = false;
		for (Papel p : permissoes) {
			if (p.getCodigo().equals(papel)) {
				encontrou = true;
			}
		}
		return encontrou;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the senha
	 */
	@JsonIgnore
	public String getSenha() {
		return senha;
	}

	/**
	 * @param senha the senha to set
	 */
	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<String> getPermissoesCodigo() {
		List<String> permissoesCodigo = new ArrayList<>();
		for (Papel papel : permissoes) {
			permissoesCodigo.add(papel.getCodigo());
		}
		return permissoesCodigo;
	}

	/**
	 * @return the permissoes
	 */
	public Set<Papel> getPermissoes() {
		return permissoes;
	}

	/**
	 * @param permissoes the permissoes to set
	 */
	public void setPermissoes(Set<Papel> permissoes) {
		this.permissoes = permissoes;
	}

	public Date getDataExpiracaoSenha() {
		return dataExpiracaoSenha;
	}

	public void setDataExpiracaoSenha(Date dataExpiracaoSenha) {
		this.dataExpiracaoSenha = dataExpiracaoSenha;
	}

	public Boolean getNotificadoExpiracaoSenha() {
		return notificadoExpiracaoSenha;
	}

	public void setNotificadoExpiracaoSenha(Boolean notificadoExpiracaoSenha) {
		this.notificadoExpiracaoSenha = notificadoExpiracaoSenha;
	}

	/**
	 * Verifica se a senha está expirada
	 */
	public Boolean isSenhaExpirada() {
		return this.dataExpiracaoSenha != null && this.dataExpiracaoSenha.before(new Date());
	}

	/**
	 * Indica se é o primeiro acesso do usuário.
	 * 
	 * @return Booleano.
	 */
	public Boolean getPrimeiroAcesso() {
		return this.getDataModificacao() == null;
	}

	/**
	 * Método que verifica se um objeto da entidade � igual ao objeto recebido como
	 * parámetro
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.permissoes;
	}

	@Override
	@JsonIgnore
	public String getPassword() {
		return this.senha;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return getAtivo();
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return getAtivo();
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return getAtivo();
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return getAtivo();
	}

	@Transient
	public String status;

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@PostLoad
	public void postLoad() {
		if (!super.getAtivo()) {
			this.status = "INATIVO";
		} else if (senha.equals("EXPIRADO") || senha.equals("BLOQUEADO")) {
			this.status = this.senha;
		} else {
			this.status = "ATIVO";
		}
	}

	public void expirarSenha() {
		senha = "EXPIRADO";
	}

	public void bloquearSenha() {
		senha = "BLOQUEADO";
	}

	/**
	 * Método para identificar se o usuário possui a responsabilidade passada como
	 * parâmetro, em algum dos papéis da sua lista de permissões.
	 *
	 * @param responsabilidade - Responsabilidade buscada.
	 */
	public boolean hasResponsabilidade(PapelResponsabilidade responsabilidade) {
		if (this.getPermissoes() != null) {
			for (Papel p : this.getPermissoes()) {
				if (p.getResponsabilidade() == null)
					continue;
				else if (p.getResponsabilidade().equals(responsabilidade)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Usuario [login=" + login + ", senha=" + senha + "]";
	}
}