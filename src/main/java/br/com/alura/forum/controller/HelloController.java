package br.com.alura.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

	@RequestMapping("/")
	@ResponseBody //diz para o spring que devemos retornar a string para o navegador, e não que o nome dessa string é o nome de um arquivo jsp, html, etc
	public String hello() {
		return "Hello World!";
	}
	
	/*
	 * Por padrão, o Spring considera que o retorno do método 
	 * é o nome da página que ele deve carregar, 
	 * mas ao utilizar a anotação @ResponseBody, 
	 * indicamos que o retorno do método deve ser serializado 
	 * e devolvido no corpo da resposta.
	 * 
	 * O Spring faz a conversão do objeto para JSON automaticamente, com o uso da biblioteca Jackson.
	 * 
	 * Se usar o @RestController no lugar do @Controller, o Spring entende que todo metodo ja vai ter o ResponseBody
	 */
}
