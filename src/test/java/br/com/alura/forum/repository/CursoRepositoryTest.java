package br.com.alura.forum.repository;


import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.alura.forum.modelo.Curso;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //informamos ao spring que é pra usar o banco de dados do aplication properties usado (por padrão, ele sempre utiliza algum banco em memoria)
@ActiveProfiles("test") //diz para o spring que quando rodar esta classe de test, é pra forçar este profile como sendo o ativo no momento. assim, ele irá rodar o application-test.properties
public class CursoRepositoryTest {

	@Autowired
	private CursoRepository repository;
	
	@Autowired
	private TestEntityManager em;
	
	@org.junit.Test
	public void deveriaCarregarUmCursoAoBuscarPeloSeuNome() {
		String nomeCurso = "HTML 5";
		
		Curso html5 = new Curso(nomeCurso, "programacao");
		em.persist(html5);
		
		Curso curso = repository.findByNome(nomeCurso);
		Assert.assertNotNull(curso);
		Assert.assertEquals("HTML 5", curso.getNome());
	}

	@org.junit.Test
	public void naoDeveriaCarregarUmCursoCujoNomeNaoEstejaCadastrado() {
		String nomeCurso = "JPA";
		Curso curso = repository.findByNome(nomeCurso);
		Assert.assertNull(curso);
	}
}
