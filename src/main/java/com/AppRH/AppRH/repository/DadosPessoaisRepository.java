package com.AppRH.AppRH.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AppRH.AppRH.models.Coopdadospessoais;
import com.AppRH.AppRH.models.Cooperado;

@Repository
public interface DadosPessoaisRepository extends JpaRepository<Coopdadospessoais, Long> {
    Iterable<Coopdadospessoais> findByCooperado(Cooperado cooperado);
}