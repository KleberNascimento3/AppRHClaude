package com.AppRH.AppRH.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.AppRH.AppRH.models.Cooperado;
//import com.AppRH.AppRH.util.PaginacaoUtil;

@Repository
public interface CooperadoRepository extends JpaRepository<Cooperado, Integer>{
		
	//Cooperado findByCoopindexcod(int coop_index_cod);
	Cooperado findByCoopmatricula(int coop_matricula);
	List<Cooperado>findByCoopnome(String coop_nome);
	
	//Optional<Cooperado> findById(Integer id);
	
	//Para Buscar
	@Query(value = "SELECT * FROM coop c", nativeQuery = true)//(Usa a anotaÃ§Ã£o para poder usar o select pata buscar todos os Cooperados)
	//List<Cooperado> encontrarTodos();
	//Preparando para paginar
	//List<Cooperado> encontrarTodos(Pageable pageable);
	Page<Cooperado> encontrarTodos(Pageable pageable);
	
	//(Usa a anotaÃ§Ã£o para poder usar o select para buscar todos os Cooperados Ativos)
	@Query(value = "SELECT * FROM coop c, coop_cadastro e WHERE c.coop_matricula = e.coop_matricula AND e.coop_cooperado = 'ATIVO'", nativeQuery = true)
	//List<Cooperado> encontrarAtivos();
	Page<Cooperado> encontrarAtivos(Pageable pageable);
	
	//(Usa a anotaÃ§Ã£o para poder usar o select para buscar todos os Cooperados Inativos)
	@Query(value = "SELECT * FROM coop c, coop_cadastro e WHERE c.coop_matricula = e.coop_matricula AND e.coop_cooperado = 'NAO'", nativeQuery = true)
	//List<Cooperado> encontrarInativos();
	Page<Cooperado> encontrarInativos(Pageable pageable);
	
		
	//(Usa a anotaÃ§Ã£o para poder usar o select para buscar todos os Cooperados por "parte" do nome)
	@Query(value = "select u from Cooperado u where u.coopnome like %?1%")
	List<Cooperado>findByCoopnomesCooperado(String coopnome);
	
	//(Usa a anotaÃ§Ã£o para poder usar o select pata buscar todos os Cooperados pelo apelido, nome ou matrÃ­cula)
	@Query(value = "SELECT * FROM coop c WHERE (c.coop_nome like CONCAT('%' :param '%') OR c.coop_matricula = :param OR c.coop_nome_guerra LIKE CONCAT('%' :param '%'))", nativeQuery = true)
	List<Cooperado>findByCoopnomesCooperados(@Param(value = "param") String coopnome);
		
	//(Usa a anotaÃ§Ã£o para poder usar o select pata buscar todos os Cooperados Ativos pelo nome ou apelido)
	@Query(value = "SELECT * FROM coop c, coop_cadastro e WHERE c.coop_matricula = e.coop_matricula AND e.coop_cooperado = 'ATIVO' And (c.coop_nome LIKE CONCAT('%':param '%') OR c.coop_matricula = :param OR c.coop_nome_guerra LIKE CONCAT('%' :param '%'))", nativeQuery = true)
	List<Cooperado>findByCoopnomesCooperadoAtivo(@Param(value = "param") String coopnome);

	//(Usa a anotaÃ§Ã£o para poder usar o select pata buscar todos os Cooperados Inativos pelo nome ou apelido)
	@Query(value = "SELECT * FROM coop c, coop_cadastro e WHERE c.coop_matricula = e.coop_matricula AND e.coop_cooperado = 'NAO' And (c.coop_nome LIKE CONCAT('%' :param '%') OR c.coop_matricula = :param OR c.coop_nome_guerra LIKE CONCAT('%' :param '%'))", nativeQuery = true)
	List<Cooperado>findByCoopnomesCooperadoInativo(@Param(value = "param") String coopnome);
	
	@Query(value="SELECT Max(c.coop_matricula) from coop c", nativeQuery = true)
	Integer achaMaior();
	
	//Busca para etiquetas
	@Query(value = "SELECT coop_matricula FROM coop order by coop_matricula asc", nativeQuery = true)
	List<Long> encontrarMatriculas();
	@Query(value = "SELECT * FROM coop c ORDER BY c.coop_nome ASC", nativeQuery = true)
	List<Cooperado> listarTodosRelatorio();

	@Query(value = "SELECT DISTINCT c.* FROM coop c INNER JOIN coop_cadastro e ON c.coop_matricula = e.coop_matricula WHERE UPPER(COALESCE(e.coop_cooperado, '')) = 'ATIVO' ORDER BY c.coop_nome ASC", nativeQuery = true)
	List<Cooperado> listarAtivosRelatorio();

	@Query(value = "SELECT DISTINCT c.* FROM coop c INNER JOIN coop_cadastro e ON c.coop_matricula = e.coop_matricula WHERE UPPER(COALESCE(e.coop_cooperado, '')) IN ('NAO', 'NÃO', 'INATIVO') ORDER BY c.coop_nome ASC", nativeQuery = true)
	List<Cooperado> listarInativosRelatorio();
}

