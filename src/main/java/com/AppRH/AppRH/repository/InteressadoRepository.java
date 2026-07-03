package com.AppRH.AppRH.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.AppRH.AppRH.models.Interessado;

@Repository
public interface InteressadoRepository extends JpaRepository<Interessado, Integer> {
    List<Interessado> findByFuncpronomeContainingIgnoreCaseOrderByFuncpronomeAsc(String nome);

    @Query("select i from Interessado i where lower(i.funcpronome) like lower(concat('%', :termo, '%')) or lower(coalesce(i.funcproemail, '')) like lower(concat('%', :termo, '%')) or lower(coalesce(i.funcprotelefone, '')) like lower(concat('%', :termo, '%')) order by i.funcpronome asc")
    List<Interessado> buscar(@Param("termo") String termo);

    List<Interessado> findAllByOrderByFuncpronomeAsc();
}
