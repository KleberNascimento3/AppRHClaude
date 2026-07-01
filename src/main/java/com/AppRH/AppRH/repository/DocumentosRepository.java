package com.AppRH.AppRH.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AppRH.AppRH.models.Coopdocumentos;
import com.AppRH.AppRH.models.Cooperado;

@Repository
public interface DocumentosRepository extends JpaRepository<Coopdocumentos, Long> {
    Iterable<Coopdocumentos> findByCooperado(Cooperado cooperado);
}