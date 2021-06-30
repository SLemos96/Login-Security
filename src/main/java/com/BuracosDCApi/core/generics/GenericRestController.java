package com.BuracosDCApi.core.generics;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@SuppressWarnings({ "rawtypes", "unchecked" })
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.OPTIONS,
		RequestMethod.DELETE })
public class GenericRestController<T extends GenericEntity, S extends GenericService> {

	@Autowired
	protected S service;

	@GetMapping
	public List<T> findAll(@RequestParam(value = "ativo", required = false) Boolean ativo) throws Exception {
		this.service.setAtivo(ativo);
		return this.service.findAll();
	}

	@GetMapping("/qtn")
	public Object count(@RequestParam(value = "ativo", required = false) Boolean ativo) throws Exception {
		this.service.setAtivo(ativo);
		return this.service.count();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<T> findById(@PathVariable Integer id,	@RequestParam(value = "ativo", required = false) Boolean ativo) {
		if (ativo != null)
			service.setAtivo(ativo);
		Optional<T> entity = service.findById(id);
		if (!entity.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(entity.get());
	}
	
	@PostMapping
	public @ResponseBody ResponseEntity<Object> save(@Valid @RequestBody T entity) throws Exception {
		T result = (T) service.save(entity);
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}

	@PutMapping
	public @ResponseBody ResponseEntity<Object> update(@Valid @RequestBody T entity) throws Exception {
		T result = (T) service.update(entity);
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable Integer id, @RequestBody(required = false) String justificativa) throws Exception {
		T result = (T) service.findById(id).get();
		service.delete(result);
	}
}
