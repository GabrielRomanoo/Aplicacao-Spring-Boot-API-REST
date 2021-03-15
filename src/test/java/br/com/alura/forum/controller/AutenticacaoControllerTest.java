package br.com.alura.forum.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest // A anotação @SpringBootTest serve para que o Spring inicialize o servidor e carregue os beans da aplicação durante a execução dos testes automatizados.
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AutenticacaoControllerTest {
	
	@Autowired
	private MockMvc mockMvc; //simula uma requisicao mvc

	@Test
	public void deveriaDevolver400CasoDadosDeAutenticacaoEstejamIncorretos() throws Exception {
		
		URI uri = new URI("/auth");
		String json = "{\"email\":\"invalido@email.com\",\"senha:123456\"}";
		
		mockMvc
		.perform(MockMvcRequestBuilders
				.post(uri)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(400));
		
	}
	
	@Test
    public void okAoReceberCredenciaisValidas() throws Exception {
        URI uri = new URI("/auth");
        String body = "{\"email\":\"aluno@email.com\",\"senha\":\"123456\"}";
        
        //vai falhar porque nao tem nenhum usuario no banco, por conta desta linha no application-test: spring.datasource.initialization-mode=never, com essa linha, ele nao carrega o data.sql

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String resposta = result.getResponse().getContentAsString();  
        
        System.out.println(resposta);
    }

}
