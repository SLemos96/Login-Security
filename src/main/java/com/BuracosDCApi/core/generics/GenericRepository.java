package com.BuracosDCApi.core.generics;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepository<T extends GenericEntity> extends JpaRepository<T, Integer> {

	@Override
	@Query(value = "select e from #{#entityName} e where e.ativo = true order by e.id asc")
	List<T> findAll();

	@Query(value = "select e from #{#entityName} e where e.id = ?1 and e.ativo = true")
	Optional<T> findById(Integer id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE #{#entityName} SET ativo=false where id = ?1")
	void deleteById(Integer id);

	@Override
	@Transactional
	@Modifying
	@Query("UPDATE #{#entityName} e SET e.ativo=false where e = ?1")
	void delete(T entity);

	@Transactional
	@Modifying
	@Query(value = "delete from #{#entityName} where id = ?1")
	void hardDeleteById(Integer id);

	@Query(value = "select count(e) from #{#entityName} e where e.ativo = true")
	long countAtivo();

	@Query(value = "select e from #{#entityName} e where e.id = ?1")
	Optional<T> findByIdIgnoreAtivo(Integer id);

	@Query(value = "select e from #{#entityName} e where e.ativo = false order by e.id asc")
	List<T> findInactiveAll();

	@Query(value = "select e from #{#entityName} e where e.ativo = false")
	Page<T> findInactiveAll(Pageable pageable);

	@Query(value = "select e from #{#entityName} e where e.id = ?1 and e.ativo = false")
	Optional<T> findInactiveById(Integer id);

	@Query(value = "select count(e) from #{#entityName} e where e.ativo = false")
	long countInactive();
}
