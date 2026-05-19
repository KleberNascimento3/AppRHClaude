package com.AppRH.AppRH.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.AppRH.AppRH.models.Funcionario;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>{
	
	Funcionario findById(long id);
	
	Funcionario findByFuncmatricula(int funcmatricula);
	
	List<Funcionario>findByFuncnome(String funcnome);
	
	//Para Buscar
	@Query(value = "SELECT * FROM func c", nativeQuery = true)
	List<Funcionario> findAll();
	
	// Query para a busca
	@Query(value = "select u from Funcionario u where u.funcnome like %?1%")
	List<Funcionario>findByFuncnomes(String funcnome);
	
	@Query(value="SELECT Max(f.func_matricula) from func f", nativeQuery = true)
	Integer achaMaior();

}