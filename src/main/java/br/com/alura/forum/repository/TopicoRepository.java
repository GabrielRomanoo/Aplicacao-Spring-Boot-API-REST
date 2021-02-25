package br.com.alura.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.alura.forum.modelo.Topico;

//como é interface, não precisa ter o @Repository
public interface TopicoRepository extends JpaRepository<Topico, Long>{

	List<Topico> findByCursoNome(String nomeCurso);	//para select em um atributo de um relacionamento da entidade

	//caso tivesse um atributo dentro de topico chamado curso nome, 
	//seria preciso fazer uma distinção na consulta, para nao dar ambiguidade
	// ist<Topico> findByCurso_Nome(String nomeCurso);	
	// o deixa explicito que '_' é um atributo do relacionamento 
	
	@Query("SELECT t FROM Topico t WHERE t.curso.nome = :nomeCurso")
	List<Topico> carregaPorNomeDoCurso(@Param("nomeCurso") String nomeCurso);	
}
