package com.AppRH.AppRH.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AppRH.AppRH.models.Autorizacao;

@Repository
public interface AutorizacaoRepository extends JpaRepository <Autorizacao, Long> {
	
	public Autorizacao findByNome(String autorizacao);

}
