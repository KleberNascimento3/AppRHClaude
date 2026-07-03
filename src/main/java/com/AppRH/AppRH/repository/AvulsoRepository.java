package com.AppRH.AppRH.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.AppRH.AppRH.models.Avulso;

@Repository
public interface AvulsoRepository extends JpaRepository<Avulso, Integer> {
    List<Avulso> findAllByOrderByNomeAsc();

    @Query("select a from Avulso a where lower(coalesce(a.nome, '')) like lower(concat('%', :termo, '%')) or lower(coalesce(a.email, '')) like lower(concat('%', :termo, '%')) or lower(coalesce(a.funcao, '')) like lower(concat('%', :termo, '%')) order by a.nome asc")
    List<Avulso> buscar(@Param("termo") String termo);
}
