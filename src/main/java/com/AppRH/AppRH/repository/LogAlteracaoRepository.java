package com.AppRH.AppRH.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.AppRH.AppRH.models.LogAlteracao;

public interface LogAlteracaoRepository extends JpaRepository<LogAlteracao, Long> {

    List<LogAlteracao> findByCoopmatriculaOrderByDataDesc(int coopmatricula);

    List<LogAlteracao> findAllByOrderByDataDesc();

}
