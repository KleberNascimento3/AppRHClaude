package com.AppRH.AppRH.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AppRH.AppRH.models.Coopendereco;
import com.AppRH.AppRH.models.Cooperado;

@Repository
public interface EnderecoRepository extends JpaRepository<Coopendereco, Long> {
	Iterable<Coopendereco>findByCooperado(Cooperado cooperado);

}
