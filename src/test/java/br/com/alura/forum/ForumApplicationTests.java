package br.com.alura.forum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import junit.framework.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest // A anotação @SpringBootTest serve para que o Spring inicialize o servidor e carregue os beans da aplicação durante a execução dos testes automatizados.
public class ForumApplicationTests {

	@Test
	public void contextLoads() {
		 org.junit.Assert.assertTrue(true);
	}

}
