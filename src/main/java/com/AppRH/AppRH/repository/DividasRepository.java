package com.AppRH.AppRH.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AppRH.AppRH.models.Cooperado;
import com.AppRH.AppRH.models.Dividas;

@Repository
public interface DividasRepository extends JpaRepository<Dividas, Long> {
	Iterable<Dividas>findByCooperado(Cooperado cooperado);

	//@Query(value = "select u from Dividas u where u.coopdatapagamento is null%")
	//List<Dividas>findByCoopdividasCooperado(Date coopdatapagamento);
}
