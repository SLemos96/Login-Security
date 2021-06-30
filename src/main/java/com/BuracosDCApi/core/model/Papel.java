package com.BuracosDCApi.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;

import com.BuracosDCApi.core.generics.GenericEntity;

@Audited
@Entity
@Table(name = "papel", schema = "public")
@AttributeOverride(name = "id", column = @Column(name = "id_papel"))
public class Papel extends GenericEntity implements GrantedAuthority {

	private static final long serialVersionUID = 1L;


	/**
	 * Código do papel
	 */
	@Column(name = "codigo", columnDefinition = "varchar(25)", nullable = true)
	private String codigo;

	/**
	 * Descrição do papel
	 */
	@Column(name = "descricao", columnDefinition = "varchar(500)", nullable = true)
	private String descricao;
	
	@ElementCollection
	@CollectionTable(name = "rotas", joinColumns = @JoinColumn(name = "id_papel"))
	@Column(name = "operacoes")
	@MapKeyColumn(name = "rota")
	private Map<String, String> rotas = new HashMap<String, String>();

	@ElementCollection
	@CollectionTable(name = "modulos", joinColumns = @JoinColumn(name = "id_papel"))
	@Column(name = "modulo")
	private List<String> modulos = new ArrayList<String>();

	private PapelResponsabilidade responsabilidade;
	
	public Papel() {
		super();
	}

	/**
	 * Construtor da entidade que recebe o ID
	 * 
	 * @param id
	 */
	public Papel(int id) {
		this.id = id;
	}

	public Papel(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * Construtor da classe que recebe o ID e a descrição
	 * 
	 * @param id
	 * @param codigo
	 */
	public Papel(int id, String codigo) {
		super();
		this.id = id;
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Entidade que verifica se um objeto da entidade é igual ao recebido como
	 * parámetro
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Papel other = (Papel) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String getAuthority() {
		return codigo;
	}

	public Map<String, String> getRotas() {
		return rotas;
	}

	public void setRotas(Map<String, String> rotas) {
		this.rotas = rotas;
	}

	public List<String> getModulos() {
		return modulos;
	}

	public void setModulos(List<String> modulos) {
		this.modulos = modulos;
	}

	public PapelResponsabilidade getResponsabilidade() {
		return responsabilidade;
	}

	public void setResponsabilidade(PapelResponsabilidade responsabilidade) {
		this.responsabilidade = responsabilidade;
	}

	@Override
	public String toString() {
		return "Papel [id=" + id + ", codigo=" + codigo + ", descricao=" + descricao + "]";
	}

}
