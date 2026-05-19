package com.AppRH.AppRH.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.AppRH.AppRH.models.Interessado;

public interface InteressadoRepository extends JpaRepository<Interessado, String>{
	
	List<Interessado> findByFuncpronome(String nome);
	//Para Buscar
		@Query(value = "SELECT * FROM profissionais p", nativeQuery = true)
		List<Interessado> findAll();
		

}
