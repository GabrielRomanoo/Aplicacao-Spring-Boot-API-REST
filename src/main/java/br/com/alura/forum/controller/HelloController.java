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
}
