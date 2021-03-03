package br.com.alura.forum.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration //o spring irá ler as configuracoes que estiverem dentro dessa classe
public class SecurityConfigurations extends WebSecurityConfigurerAdapter { 
	
	@Autowired
	private AutenticacaoService autenticacaoService;
	
	//Configuracoes de Autenticacao
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(autenticacaoService) //uerDetailsService diz para o spring qual é a classe que tem a logica de autenticacao
			.passwordEncoder(new BCryptPasswordEncoder());
	}		
	
	//Configuracoes de Autorizacao
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/topicos").permitAll() //listar topicos
			.antMatchers(HttpMethod.GET, "/topicos/*").permitAll() //detalhar topicos
			.anyRequest().authenticated() //qualquer outra requisicao(url) precisa estar autenticada
		.and()
			.formLogin(); //fala pro spring gerar um formulario de autenticacao e um controller que recebe as requisicoes desse formualario
	}
	
	//Configuracoes de recursos estaticos (js, css, imagens, etc)
	@Override
	public void configure(WebSecurity web) throws Exception {
	}
	
}
