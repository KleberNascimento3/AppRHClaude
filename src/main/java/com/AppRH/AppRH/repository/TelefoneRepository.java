package com.AppRH.AppRH.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

//import java.util.List;
import com.AppRH.AppRH.models.Cooperado;
import com.AppRH.AppRH.models.Telefone;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long>{
	
	Iterable<Telefone>findByCooperado(Cooperado cooperado);
	
	//Telefone findByCoopmatricula(int coop_matricula);
	
	Telefone findByCooptelindexcod(int coop_tel_index_cod);
	
	Telefone findBycooptelnumero(String coop_tel_numero);
	
	//Para Buscar
	//@Query(value = "select t from Telefone t where t.coop_tel_numero like %?1%")
	//List<Telefone>findByCoopteltipo(String coop_tel_tipo);

}
