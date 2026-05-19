package com.AppRH.AppRH.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.AppRH.AppRH.models.Dependentes;
import com.AppRH.AppRH.models.Funcionario;

public interface DependenteRepository extends JpaRepository<Dependentes, Long> {

	Iterable<Dependentes> findByFuncionario(Funcionario funcionario);

	// para o método delete por CPF
	Dependentes findByFuncdepcpf(String funcdepcpf);
	
	Dependentes findById(long id);
	List<Dependentes> findByFuncdepnome(String funcdepnome);

	// Query para a busca
	@Query(value = "select u from Dependentes u where u.funcdepnome like %?1%")
	List<Dependentes> findByFuncdepnomesdependentes(String funcdepnome);

}