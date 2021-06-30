package com.BuracosDCApi.core.generics;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unused")
public class GenericService<T extends GenericEntity, R extends GenericRepository<T>> {

	@Autowired
	protected R repository;
	
	// false caso queira listar objetos inativos
	private Boolean ativo = true; 

	public void validate(T entity) throws Exception {
	}

	
	public void validateOnSave(T entity) throws Exception {
	}

	public void validateOnUpdate(T entity) throws Exception {
	}

	public void validateOnDelete(T entity) throws Exception {
	}

	public T preSaveOrUpdate(T entity) throws Exception {
		return entity;
	}

	public T preSave(T entity) throws Exception {
		return entity;
	}

	public T preUpdate(T entity) throws Exception {
		return entity;
	}

	public T posSave(T entity) throws Exception {
		return entity;
	}

	public T posUpdate(T entity) throws Exception {
		return entity;
	}

	public T posSaveOrUpdate(T entity) throws Exception {
		return entity;
	}

	public T saveOrUpdate(T entity) throws Exception {
		return entity.getId() == null ? save(entity) : update(entity);
	}

	public T save(T obj) throws Exception {
		T entity = this.preSaveOrUpdate(obj);
		entity = this.preSave(entity);
		this.validate(entity);
		this.validateOnSave(entity);
		entity = repository.save(obj);
		entity = this.posSave(entity);
		entity = this.posSaveOrUpdate(entity);
		return entity;
	}

	public T update(T obj) throws Exception {
		T entity = this.preSaveOrUpdate(obj);
		entity = this.preUpdate(entity);
		this.validate(entity);
		this.validateOnUpdate(entity);

		
		entity = repository.save(entity);
		entity = this.posUpdate(entity);
		entity = this.posSaveOrUpdate(entity);
		return entity;
	}
	
	
	public void delete(T obj) throws Exception {
		this.validateOnDelete(obj);
		obj.setAtivo(false);
		T entity = this.repository.findById(obj.getId()).get();
		repository.save(obj);
	}

	public void deleteById(Integer id) throws Exception {
		this.validateOnDelete(repository.findById(id).get());
		repository.deleteById(id);
	}

	public void softDelete(T entity) throws Exception {
		this.validateOnDelete(entity);
		repository.delete(entity);
	}

	public void hardDeleteById(Integer id) throws Exception {
		this.validateOnDelete(repository.findById(id).get());
		repository.hardDeleteById(id);
	}

	public Optional<T> findById(Integer id) {
		if (ativo == null) {
			return repository.findByIdIgnoreAtivo(id);
		} else {
			if (ativo) {
				return repository.findById(id);
			} else {
				Optional<T> result = repository.findInactiveById(id);
				ativo = true;
				return result;
			}
		}
	}

	public void setAtivo(boolean newValue) {
		this.ativo = newValue;
	}
	
	public List<T> findAll() {
		if (ativo) {
			return repository.findAll();
		} else {
			List<T> result = repository.findInactiveAll();
			ativo = true;
			return result;
		}
	}
	
	public long count() {
		return this.ativo ? repository.countAtivo() : repository.countInactive();
	}

}
