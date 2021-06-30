package com.BuracosDCApi.core.model;

import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.BuracosDCApi.core.generics.GenericEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Audited
@Entity
@Table(name = "arquivo", schema = "public")
@AttributeOverride(name = "id", column = @Column(name = "id_arquivo"))
public class Arquivo extends GenericEntity {

	private static final long serialVersionUID = -5202984702209951273L;

	/**
	 * Nome do arquivo
	 */
	private String nome;

	/**
	 * Nome original do arquivo
	 */
	private String nomeOriginal;

	/**
	 * Descrição do arquivo
	 */
	private String descricao;
	
	private String formato;
	
	private Boolean isDirectory = false;
	
	private Boolean isRoot = false;

	/**
	 * Caminho do arquivo no servidor
	 */
	private String path;
	
	@OneToMany(mappedBy="diretorio")
	public Set<Arquivo> arquivos;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="diretorio")
	public Arquivo diretorio;

	/**
	 * Construtor da entidade
	 */
	public Arquivo() {
		super();
	}

	/**
	 * Construtor da classe que recebe os atributos como parámetros
	 * 
	 * @param nome
	 * @param descricao
	 * @param path
	 */
	public Arquivo(String nome, String descricao, String path, String nomeOriginal) {
		this.nome = nome;
		this.nomeOriginal = nomeOriginal;
		this.descricao = descricao;
		this.path = path;
	}

	public Arquivo(String nome, String path) {
		this.nome = nome;
		this.path = path;
	}

	public Arquivo(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the nomeOriginal
	 */
	public String getNomeOriginal() {
		return nomeOriginal;
	}

	/**
	 * @param nomeOriginal the nomeOriginal to set
	 */
	public void setNomeOriginal(String nomeOriginal) {
		this.nomeOriginal = nomeOriginal;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arquivo other = (Arquivo) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Arquivo [nome=" + nome + ", nomeOriginal=" + nomeOriginal + ", descricao=" + descricao + ", path="
				+ path + ", arquivos=" + arquivos + ", diretorio=" + diretorio + "]";
	}

	public Set<Arquivo> getArquivos() {
		return arquivos;
	}

	public void setArquivos(Set<Arquivo> arquivos) {
		this.arquivos = arquivos;
	}

	public Arquivo getDiretorio() {
		return diretorio;
	}

	public void setDiretorio(Arquivo diretorio) {
		this.diretorio = diretorio;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public Boolean getIsDirectory() {
		return isDirectory;
	}

	public void setIsDirectory(Boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	public Boolean getIsRoot() {
		return isRoot;
	}

	public void setIsRoot(Boolean isRoot) {
		this.isRoot = isRoot;
	}
	
	@JsonIgnore
	public String getDirName() {
		return path != null ? path + nome + "/" : nome + "/";
	}

}
